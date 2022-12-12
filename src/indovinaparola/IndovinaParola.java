/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package indovinaparola;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author suppa_nicolo
 */
public class IndovinaParola {

    private String parola;
    ArrayList<String> tpar =new ArrayList();
    private String parolaNascosta = nascondiParola(parola);
    private boolean playState = true;
    Server server;

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

    public void check(String str) {
        if (!playState) {
            if (str.equalsIgnoreCase("start")) {
                Start();
                return;
            }
            return;
        }

        if (str.equalsIgnoreCase("jolly")) {
            //Deve essere inviato a tutti i client-->("Parola: " + parola + "\nGioco terminato, digitare 'Start' per iniziare una nuova partita");
            playState = false;
            return;
        }

        if (str.equalsIgnoreCase(parola)) {
            //Deve essere inviato a tutti i client-->("Parola indovinata! Digitare 'Start' per iniziare una nuova partita");
            playState = false;
            return;
        }

        String temp = "";
        for (int i = 0; i < parola.length(); i++) {
            if (parola.charAt(i) == str.charAt(i)) {
                temp += parola.charAt(i);
            } else {
                temp += '#';
            }
        }
        //Deve essere inviato a tutti i client-->("Parola: " + temp);
    }

    private void Start() {
        playState = true;
        //la parola nascosta(#####) va fatta vedere a tutti i giocatori
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

}
