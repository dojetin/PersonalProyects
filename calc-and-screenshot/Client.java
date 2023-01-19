package DavidChat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Cliente {
	public static void main(String[] args) throws IOException {
		Scanner s = new Scanner(System.in);
		int p = 7777;
		Socket socket = new Socket("192.168.43.35", p);

		// salida
		OutputStream outputStream = socket.getOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

		InputStream inputStream = socket.getInputStream();
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		String intro = "false";

		do {
			intro = dataInputStream.readUTF();
			System.out.println(intro);
			String pin = s.nextLine();
			dataOutputStream.writeUTF(pin);
			intro = dataInputStream.readUTF();
		} while (intro.equals("false"));

		String msg = "";
		msg = dataInputStream.readUTF();
		System.out.println(msg);
		do {
			msg = dataInputStream.readUTF();
			System.out.println(msg);
			msg = dataInputStream.readUTF();
			System.out.println(msg);
			msg = dataInputStream.readUTF();
			System.out.println(msg);
			int x = s.nextInt();
			dataOutputStream.writeInt(x);
			int y=1;

			switch (x) {
			case 1:
				do {
					msg = dataInputStream.readUTF();
					System.out.println(msg);
					msg = dataInputStream.readUTF();
					System.out.println(msg);
					msg = dataInputStream.readUTF();
					System.out.println(msg);
					msg = dataInputStream.readUTF();
					System.out.println(msg);
					msg = dataInputStream.readUTF();
					System.out.println(msg);
					y = s.nextInt();
					dataOutputStream.writeInt(y);
				}while(y<1||y>4);
				msg = dataInputStream.readUTF();
				System.out.println(msg);

				int s1 = s.nextInt();
				int s2 = s.nextInt();
				dataOutputStream.writeInt(s1);
				dataOutputStream.writeInt(s2);
				msg = dataInputStream.readUTF();

				System.out.print("\033[H\033[2J");  
				System.out.flush(); 

				System.out.println(msg);


				break;
			case 2:
				try {
					recibir(socket,dataInputStream);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case 0:
				socket.close();
				break;
			}
		} while (!msg.equals("p"));

		dataOutputStream.close();

		System.out.println("Cerrando el Socket.");
		socket.close();
	}


	private static void recibir(Socket  s,InputStream dos) throws IOException, ClassNotFoundException {
		ObjectInputStream entrada;
		entrada = new ObjectInputStream( s.getInputStream() );
			byte[] bytesImagen = (byte[]) entrada.readObject();
			ByteArrayInputStream entradaImagen = new ByteArrayInputStream(bytesImagen);
			BufferedImage bufferedImage = ImageIO.read(entradaImagen);
			String mensaje=JOptionPane.showInputDialog("Dime el nombre del fichero");
			String nombreFichero=System.getProperty("user.home")+File.separatorChar+mensaje+".jpg";
			System.out.println("Generando el fichero en: "+nombreFichero );
			FileOutputStream out = new FileOutputStream(nombreFichero);

			ImageIO.write(bufferedImage, "jpg", out);
	}

}
