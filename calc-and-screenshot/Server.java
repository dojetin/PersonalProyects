import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;


public class Server {
	public static void main(String[] args) throws IOException {
		// Especificamos el Puerto
		ServerSocket ss = new ServerSocket(7777);
		String pin = "jejeje";
		System.out.println("ServerSocket esperando conexiones ...");
		Socket socket = ss.accept();
		System.out.println("Conexion desde " + socket + "!");

		OutputStream outputStream = socket.getOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

		InputStream inputStream = socket.getInputStream();
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		while (!pin.contentEquals("Valtorre")) {
			dataOutputStream.writeUTF("Introduce el pin");
			pin = dataInputStream.readUTF();
			System.out.println(pin);
			if (!pin.contentEquals("Valtorre")) {
				dataOutputStream.writeUTF("false");
			}
		}
		System.out.println("GOL");
		System.out.println("GOL");

		dataOutputStream.writeUTF("true");
		System.out.println("Contraseña correcta");
		dataOutputStream.writeUTF("Bienvenido al Menú Ramon Guapo");

		int numerito = -1;
		while (numerito != 0) {
			dataOutputStream.writeUTF("1. Calculadora");
			dataOutputStream.writeUTF("2. Imagen");
			dataOutputStream.writeUTF("0. Salir");
			numerito = dataInputStream.readInt();
			System.out.println(numerito);

			switch (numerito) {
			case 1:
				int calc;
				do {
					dataOutputStream.writeUTF("Ahora estas en la calculadora");
					dataOutputStream.writeUTF("1. Sumar");
					dataOutputStream.writeUTF("2. Restar");
					dataOutputStream.writeUTF("3. Multiplicar");
					dataOutputStream.writeUTF("4. Dividir");
					calc = dataInputStream.readInt();
				} while (calc<1||calc>4);

				int num1;
				int num2;
				int solucion;
				dataOutputStream.writeUTF("Dime dos numeros");
				num1 = dataInputStream.readInt();
				num2 = dataInputStream.readInt();
				switch (calc) {
				case 1:
					solucion=(int)num1+(int)num2;
					dataOutputStream.writeUTF("La suma es "+solucion);
					break;
				case 2:
					solucion=(int)num1-(int)num2;
					dataOutputStream.writeUTF("La resta es "+solucion);
					break;
				case 3:
					solucion=(int)num1*(int)num2;
					dataOutputStream.writeUTF("La multiplicacion da "+solucion);
					break;
				case 4:
					solucion=(int)num1/(int)num2;
					dataOutputStream.writeUTF("La division da "+solucion);
					break;
				default:
					break;
				}
				break;
			case 2:
				enviarImagen(socket);
				break;
			case 0:
				ss.close();
				break;
			default:
				break;
			}
		}



	}
	private static void enviarImagen(Socket cliente) throws IOException {
		final String servidorChat;
		final ObjectOutputStream salida;
		salida = new ObjectOutputStream( cliente.getOutputStream() );
		salida.flush(); // vacíar búfer de salida para enviar información de encabezado
		Rectangle rectangleTam = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		try {
			Robot robot = new Robot();
			BufferedImage bufferedImage = robot.createScreenCapture(rectangleTam);
			ByteArrayOutputStream salidaImagen = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "jpg", salidaImagen);
			byte[] bytesImagen = salidaImagen.toByteArray();
			salida.writeObject( bytesImagen );
			salida.flush();
			System.out.println( "Se ha enviado la imagen" );
		} catch (AWTException e) {
			e.printStackTrace();
		} // procesar los problemas que pueden ocurrir al enviar el objeto
		catch ( IOException excepcionES ) {
			System.out.println( "\nError al escribir el objeto" );
		}
	}  
}

