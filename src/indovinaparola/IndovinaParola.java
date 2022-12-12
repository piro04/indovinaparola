/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package indovinaparola;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author suppa_nicolo
 */
public class IndovinaParola {

    private String parola = "paguro";
    ArrayList<String> array= new ArrayList<>;
    private String parolaNascosta = nascondiParola(parola);
    private boolean playState = false;
    Server server;

    public IndovinaParola() {
        public IndovinaParola() throws FileNotFoundException, IOException {
        FileReader f;
        f = new FileReader("parole.txt");

        BufferedReader b;
        b = new BufferedReader(f);

        String s;

        while (true) {
            s = b.readLine();
            if (s == null) {
                break;
            }
            tpar.add(s);
        }
        
        int random =  (int) (0 +  (Math.random() * 650000));
        parola = tpar.get(random);

    }

    public void check(String str, ClientHandler c) {
        if (!playState) {
            if (str.equalsIgnoreCase("start")) {
                Start();
                c.forwardToAllClients(parolaNascosta);
                return;
            }
            return;
        }

        if (str.equalsIgnoreCase("jolly")) {
            c.forwardToAllClients("Parola: " + parola + "\nGioco terminato, digitare 'Start' per iniziare una nuova partita");
            playState = false;
            return;
        }

        c.tentativi++;
        System.out.println("tentativi " + c.name + ": " + c.tentativi);
        if (str.equalsIgnoreCase(parola)) {
            c.forwardToAllClients("Parola indovinata! Digitare 'Start' per iniziare una nuova partita");
            playState = false;
            controllaPunteggio(c.name, c.tentativi);
            c.forwardToAllClients(visualizzaClassifica());
            return;
        }

        String temp = "";
        for (int i = 0; i < parola.length(); i++) {

            if (i >= str.length()) {
                temp += '#';
                continue;
            }

            if (parola.charAt(i) == str.charAt(i)) {
                temp += parola.charAt(i);
            } else {
                temp += '#';
            }
        }
        c.forwardToAllClients(c.name + ": " + str);
        c.forwardToAllClients("Parola: " + temp);
    }

    private void Start() {
        playState = true;
        
    }

    private String nascondiParola(String parola) {
        String p = "";
        for (int i = 0; i < parola.length(); i++) {
            p += '#';
        }
        return p;
    }

    public boolean isPlayState() {
        return playState;
    }

    private void controllaPunteggio(String name, int tentativi) {
        List<List<String>> classifica = new ArrayList<>();
        try ( Scanner scanner = new Scanner(new File("classifica.csv"));) {
            while (scanner.hasNextLine()) {
                classifica.add(getRecordFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IndovinaParola.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<String> record = new ArrayList<>();
        boolean trovato = false;
        for (int i = 0; i < classifica.size(); i++) {
            record = classifica.get(i);
            if (!record.get(0).equalsIgnoreCase(name)) {
                continue;
            }
            trovato = true;
            if (tentativi < Integer.parseInt(record.get(1))) {
                record.set(1, tentativi + "");
                aggiornaClassifica(classifica);
                return;
            }
        }
        if (trovato) {
            return;
        }
        record = new ArrayList<>();
        record.add(name);
        record.add(tentativi + "");
        classifica.add(record);
        aggiornaClassifica(classifica);
    }

    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try ( Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(";");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    private void aggiornaClassifica(List<List<String>> classifica) {
        boolean ordinato = false;
        int i = 0;
        while (i < classifica.size() && !ordinato) {
            ordinato = true;
            for (int j = 0; j < classifica.size() - j; j++) {
                if (Integer.parseInt(classifica.get(j).get(1)) > Integer.parseInt(classifica.get(j + 1).get(1))) {
                    ordinato = false;
                    List<String> temp = new ArrayList<String>();
                    temp = classifica.get(j);
                    classifica.set(j, classifica.get(j + 1));
                    classifica.set(j + 1, temp);
                }
            }
            i++;
        }

        writeToFile(toCSV(classifica));
    }

    private String toCSV(List<List<String>> classifica) {
        String csv = "";
        for (int i = 0; i < classifica.size(); i++) {
            csv += classifica.get(i).get(0) + ";" + classifica.get(i).get(1) + "\n";
        }
        return csv;
    }

    private void writeToFile(String str) {
        try {
            FileWriter myWriter = new FileWriter("classifica.csv");
            myWriter.write(str);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String visualizzaClassifica() {
        List<List<String>> classifica = new ArrayList<>();
        try ( Scanner scanner = new Scanner(new File("classifica.csv"));) {
            while (scanner.hasNextLine()) {
                classifica.add(getRecordFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IndovinaParola.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String temp = "Classifica:\n";
        for (int i = 0; i < classifica.size(); i++) {
            temp += i+1 + ")" + classifica.get(i).get(0) + " " + classifica.get(i).get(1) + "\n";
        }
        return temp;
    }
}
