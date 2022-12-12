/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package indovinaparola;

/**
 *
 * @author suppa_nicolo
 */
public class IndovinaParola {

    private String parola = "paguro";
    private String parolaNascosta = nascondiParola(parola);
    private boolean playState = false;
    Server server;

    public IndovinaParola() {
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

        if (str.equalsIgnoreCase(parola)) {
            c.forwardToAllClients("Parola indovinata! Digitare 'Start' per iniziare una nuova partita");
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
        c.forwardToAllClients("Parola: " + temp);
    }

    private void Start() {
        playState = true;
    }
    
    private String nascondiParola(String parola){
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
