package com.sergio.calimero_test;

import java.net.InetSocketAddress;
import java.util.Scanner;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.KNXException;
import tuwien.auto.calimero.KNXFormatException;
import tuwien.auto.calimero.KNXTimeoutException;
import tuwien.auto.calimero.link.KNXLinkClosedException;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.medium.TPSettings;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessCommunicatorImpl;

/**
 * Hello world!
 *
 */
public class App {
	// Address of your KNXnet/IP server. Replace the IP host or address as
	// necessary.
	private static final String remoteHost = "192.168.1.11";

	/**
	 * We will read a boolean from this KNX datapoint group address, replace the
	 * address string with one of yours. Make sure this datapoint exists,
	 * otherwise you will get a read timeout!
	 */
	private static final String group = "1/1/10";

	private static ProcessCommunicator pc;

	public static void main(final String[] args) {

		// Create our network link, and pass it to a process communicator
		try

		{
			KNXNetworkLink knxLink = KNXNetworkLinkIP.newTunnelingLink(null, new InetSocketAddress(remoteHost, 3671),
					false, TPSettings.TP1);
			pc = new ProcessCommunicatorImpl(knxLink);
			Scanner teclado = new Scanner(System.in);

			int op1 = 0;

			do {

				System.out.println("1 - Turn On Light");
				System.out.println("2 - Turn Off Light");
				System.out.println("3 - Light status");
				System.out.println("4 - Shutter down");
				System.out.println("5 - Shutter up");
				System.out.println("6 - Shutter stop");
				op1 = Integer.parseInt(teclado.next());

				switch (op1) {
				case 1:
					turnOnLight();
					break;
				case 2:
					turnOffLight();
					break;
				case 3:
					getLigthValue();
					break;
				case 4:
					downShutter();
					break;
				case 5:
					upShutter();
					break;
				case 6:
					stopShutter();
					break;

				default:
					System.out.println("No valid op ");

				}

			} while (op1 != 7);

		} catch (KNXException | InterruptedException e) {
			System.out.println("Error reading KNX datapoint: " + e.getMessage());
		}
	}

	private static void turnOnLight() throws KNXTimeoutException, KNXLinkClosedException, KNXFormatException {
		pc.write(new GroupAddress("1/1/10"), true);
	}

	private static void turnOffLight() throws KNXTimeoutException, KNXLinkClosedException, KNXFormatException {
		pc.write(new GroupAddress("1/1/10"), false);
	}

	private static void upShutter() throws KNXTimeoutException, KNXLinkClosedException, KNXFormatException {
		pc.write(new GroupAddress("1/2/0"), false);
	}

	private static void downShutter() throws KNXTimeoutException, KNXLinkClosedException, KNXFormatException {
		pc.write(new GroupAddress("1/2/0"), true);
	}

	private static void stopShutter() throws KNXTimeoutException, KNXLinkClosedException, KNXFormatException {
		pc.write(new GroupAddress("1/2/1"), true);
	}

	private static void getLigthValue() throws KNXException, InterruptedException {
		final boolean value = pc.readBool(new GroupAddress("1/1/20"));
		System.out.println("Light: " + value);
	}

	private static void getShutterValue() throws KNXException, InterruptedException {
		final String value = pc.readString(new GroupAddress("1/2/4"));
		System.out.println("Shutter: " + value);
	}

}
