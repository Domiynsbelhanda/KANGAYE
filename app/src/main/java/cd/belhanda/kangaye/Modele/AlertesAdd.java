package cd.belhanda.kangaye.Modele;

public class AlertesAdd {
    private String Categorie;
    private String Details;
    private String Heure;
    private String Date;
    private String KeyALertes;
    private String Emetteur;
    private double Distance;
    private String Pseudo;
    private String Profil;
    private String Telephone;
    private double Longitude;
    private double Latitude;
    private boolean Vu;
    private boolean Recu;

    public AlertesAdd(String categorie, String details, String heure, String date, String keyALertes, String emetteur, double longitude, double latitude) {
        Categorie = categorie;
        Details = details;
        Heure = heure;
        Date = date;
        KeyALertes = keyALertes;
        Emetteur = emetteur;
        Longitude = longitude;
        Latitude = latitude;
    }

    public AlertesAdd(double distance, String pseudo, String profil, String telephone) {
        Distance = distance;
        Pseudo = pseudo;
        Profil = profil;
        Telephone = telephone;
    }

    public AlertesAdd(double distance, String pseudo, String profil, String telephone, double longitude, double latitude) {
        Distance = distance;
        Pseudo = pseudo;
        Profil = profil;
        Telephone = telephone;
        Longitude = longitude;
        Latitude = latitude;
    }

    public AlertesAdd(double distance, String pseudo, String profil, String telephone, double longitude, double latitude, boolean vu, boolean recu) {
        Distance = distance;
        Pseudo = pseudo;
        Profil = profil;
        Telephone = telephone;
        Longitude = longitude;
        Latitude = latitude;
        Vu = vu;
        Recu = recu;
    }

    public AlertesAdd(String categorie, String details, String heure, String date, String keyALertes, double distance, String pseudo, String profil, String telephone, double longitude, double latitude, boolean vu, boolean recu) {
        Categorie = categorie;
        Details = details;
        Heure = heure;
        Date = date;
        KeyALertes = keyALertes;
        Distance = distance;
        Pseudo = pseudo;
        Profil = profil;
        Telephone = telephone;
        Longitude = longitude;
        Latitude = latitude;
        Vu = vu;
        Recu = recu;
    }

    public AlertesAdd() {
    }

    public AlertesAdd(String categorieMenaces, String detailMenaces, String heuresEnvoie, String datesEnvoie, String nom, String key, double longitude, double latitude, String pseudoss, String telephoness) {
        Categorie = categorieMenaces;
        Details = detailMenaces;
        Heure = heuresEnvoie;
        Date = datesEnvoie;
        KeyALertes = nom;
        Emetteur = key;
        Longitude = longitude;
        Latitude = latitude;
        Pseudo = pseudoss;
        Telephone = telephoness;
    }

    public AlertesAdd(String categorieMenaces, String detailMenaces, String heuresEnvoie, String datesEnvoie, String nom, String key, double longitude, double latitude, String pseudoss, String telephoness, boolean b, boolean b1, double distance, String profil) {
        Categorie = categorieMenaces;
        Details = detailMenaces;
        Heure = heuresEnvoie;
        Date = datesEnvoie;
        KeyALertes = nom;
        Emetteur = key;
        Longitude = longitude;
        Latitude = latitude;
        Pseudo = pseudoss;
        Telephone = telephoness;
        Vu = b;
        Recu = b1;
        Distance = distance;
        Profil = profil;
    }

    public String getCategorie() {
        return Categorie;
    }

    public void setCategorie(String categorie) {
        Categorie = categorie;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getHeure() {
        return Heure;
    }

    public void setHeure(String heure) {
        Heure = heure;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getKeyALertes() {
        return KeyALertes;
    }

    public void setKeyALertes(String keyALertes) {
        KeyALertes = keyALertes;
    }

    public String getEmetteur() {
        return Emetteur;
    }

    public void setEmetteur(String emetteur) {
        Emetteur = emetteur;
    }

    public double getDistance() {
        return Distance;
    }

    public void setDistance(double distance) {
        Distance = distance;
    }

    public String getPseudo() {
        return Pseudo;
    }

    public void setPseudo(String pseudo) {
        Pseudo = pseudo;
    }

    public String getProfil() {
        return Profil;
    }

    public void setProfil(String profil) {
        Profil = profil;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public boolean isVu() {
        return Vu;
    }

    public void setVu(boolean vu) {
        Vu = vu;
    }

    public boolean isRecu() {
        return Recu;
    }

    public void setRecu(boolean recu) {
        Recu = recu;
    }
}
