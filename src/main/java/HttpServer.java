import java.net.*;
import java.io.*;
public class HttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(getPort());
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        while(running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;
            String file = "";
            boolean firstLine = true;
            while ((inputLine = in.readLine()) != null) {
                if (firstLine) {
                    file = inputLine.split(" ")[1];
                    System.out.println("File: " + file);
                    firstLine = false;
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            outputLine = "";
            if(file.contains("/clima")){
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "<head>\n"
                        + "<meta charset=\"UTF-8\">\n"
                        + "<title>Weather-AREP</title>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<h1>Clima</h1>\n"
                        + "<div>"
                        + "<input type=\"text\" id=city placeholder=\"Ciudad\">"
                        +"<script> "
                        +" function climajson(){\n" +
                        "    fetch('http://localhost:4567/consulta?=london')\n" +
                        "          .then(response => response.json())\n" +
                        "          .then(json => console.log(json))\n" +
                        "    }\n"
                        +"</script>"
                        + "<button type=\"button\" onclick=\"climajson()\">Search</button>"
                        + "</div>"
                        + "</body>\n"
                        + "</html>\n" ;
            }
            else if(file.contains("/consulta")){
                String country = file.substring(file.lastIndexOf("=") + 1);
                String res = ConsumeAPI.getClima(country);
                System.out.println("Country: " + country);
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "<head>\n"
                        + "<meta charset=\"UTF-8\">\n"
                        + "<title>Title of the document</title>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<h3>" + res +
                        "</h3>\n"
                        + "</body>\n"
                        + "</html>\n" ;
            }
            else {
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "<head>\n"
                        + "<meta charset=\"UTF-8\">\n"
                        + "<title>Title of the document</title>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<h1>Mi propio mensaje</h1>\n"
                        + "</body>\n"
                        + "</html>\n";
            }
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();

    }
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set
    }
}
