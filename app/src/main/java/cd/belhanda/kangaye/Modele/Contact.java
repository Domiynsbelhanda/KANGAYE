package cd.belhanda.kangaye.Modele;

public class Contact {
    String pseudo;
    String telephone;
    String profil;

    public Contact() {
    }

    public Contact(String pseudo, String telephone, String profil) {
        this.pseudo = pseudo;
        this.telephone = telephone;
        this.profil = profil;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }
}
