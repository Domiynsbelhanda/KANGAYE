package cd.belhanda.kangaye.Modele;

public class Inscription_Modele {
    private String nom;
    private String prenom;
    private String pseudo;
    private String telephone;
    private String mot_de_passe;
    private String profil;
    private String mail;

    public Inscription_Modele() {
    }

    public Inscription_Modele(String nom, String prenom, String pseudo, String telephone, String mot_de_passe, String profil) {
        this.nom = nom;
        this.prenom = prenom;
        this.pseudo = pseudo;
        this.telephone = telephone;
        this.mot_de_passe = mot_de_passe;
        this.profil = profil;
    }

    public Inscription_Modele(String nom, String prenom, String pseudo, String telephone, String mot_de_passe, String profil, String mail) {
        this.nom = nom;
        this.prenom = prenom;
        this.pseudo = pseudo;
        this.telephone = telephone;
        this.mot_de_passe = mot_de_passe;
        this.profil = profil;
        this.mail = mail;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
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

    public String getMot_de_passe() {
        return mot_de_passe;
    }

    public void setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}