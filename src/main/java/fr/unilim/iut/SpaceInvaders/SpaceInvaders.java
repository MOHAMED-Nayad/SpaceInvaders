package fr.unilim.iut.SpaceInvaders;

import fr.unilim.iut.SpaceInvaders.moteurjeu.Commande;
import fr.unilim.iut.SpaceInvaders.moteurjeu.Jeu;
import fr.unilim.iut.SpaceInvaders.utils.DebordementEspaceJeuException;
import fr.unilim.iut.SpaceInvaders.utils.HorsEspaceJeuException;

public class SpaceInvaders implements Jeu {
	int longueur;
	int hauteur;
	Vaisseau vaisseau;
	Missile missile;

	public SpaceInvaders(int longueur, int hauteur) {
		this.longueur = longueur;
		this.hauteur = hauteur;
	}

	private boolean estDansEspaceJeu(int x, int y) {
		return (x < longueur && x >= 0) && (y < hauteur && y >= 0);
	}

	public String recupererEspaceJeuDansChaineASCII() {
		StringBuilder espaceDeJeu = new StringBuilder();
		for (int y = 0; y < hauteur; y++) {
			for (int x = 0; x < longueur; x++) {
				espaceDeJeu.append(recupererMarqueDeLaPosition(x, y));
			}

			espaceDeJeu.append(Constante.MARQUE_FIN_LIGNE);
		}

		return espaceDeJeu.toString();
	}

	private char recupererMarqueDeLaPosition(int x, int y) {
		char marque;
		if (this.aUnVaisseauQuiOccupeLaPosition(x, y)) 
			marque = Constante.MARQUE_VAISSEAU;
		else if (this.aUnMissileQuiOccupeLaPosition(x, y)) 
			marque = Constante.MARQUE_MISSILE;
		 else 
			marque = Constante.MARQUE_VIDE;
		
		return marque;
	}

	private boolean aUnMissileQuiOccupeLaPosition(int z, int t) {
		return this.aUnMissile() && vaisseau.occupeLaPosition(z, t);
	}

	private boolean aUnMissile() {
		return null != missile;
	}

	private boolean aUnVaisseauQuiOccupeLaPosition(int x, int y) {
		return this.aUnVaisseau() && vaisseau.occupeLaPosition(x, y);
	}

	public boolean aUnVaisseau() {
		return null != vaisseau;
	}

	public void deplacerVaisseauVersLaDroite() {
		if (vaisseau.abscisseLaPlusADroite() < (longueur - 1)) {
			vaisseau.seDeplacerVersLaDroite();
			if (!estDansEspaceJeu(vaisseau.abscisseLaPlusADroite(), vaisseau.ordonneeLaPlusHaute())) {
				vaisseau.positionner(longueur - vaisseau.longueur(), vaisseau.ordonneeLaPlusHaute());
			}
		}
	}

	public void deplacerVaisseauVersLaGauche() {
		if (0 < vaisseau.abscisseLaPlusAGauche())
			vaisseau.seDeplacerVersLaGauche();
		if (!estDansEspaceJeu(vaisseau.abscisseLaPlusAGauche(), vaisseau.ordonneeLaPlusHaute())) {
			vaisseau.positionner(0, vaisseau.ordonneeLaPlusHaute());
		}
	}

	public void positionnerUnNouveauVaisseau(Dimension dimension, Position position, int vitesse) {

		int x = position.abscisse();
		int y = position.ordonnee();
		int longueurVaisseau = dimension.longueur();
		int hauteurVaisseau = dimension.hauteur();

		if (!estDansEspaceJeu(x, y))
			throw new HorsEspaceJeuException("La position du vaisseau est en dehors de l'espace jeu");

		if (!estDansEspaceJeu(x + longueurVaisseau - 1, y))
			throw new DebordementEspaceJeuException(
					"Le vaisseau déborde de l'espace jeu vers la droite à cause de sa longueur");
		if (!estDansEspaceJeu(x, y - hauteurVaisseau + 1))
			throw new DebordementEspaceJeuException(
					"Le vaisseau déborde de l'espace jeu vers le bas à cause de sa hauteur");

		vaisseau = new Vaisseau(dimension, position, vitesse);
	}

	@Override
	public void evoluer(Commande commandeUser) {
		if (commandeUser.droite) {
			this.deplacerVaisseauVersLaDroite();
		}
		if (commandeUser.gauche) {
			this.deplacerVaisseauVersLaGauche();
		}
	}

	@Override
	public boolean etreFini() {
		return false;
	}

	public void initialiserJeu() {
		Position positionVaisseau = new Position(this.longueur / 2, this.hauteur - 1);
		Dimension dimensionVaisseau = new Dimension(Constante.VAISSEAU_LONGUEUR, Constante.VAISSEAU_HAUTEUR);
		positionnerUnNouveauVaisseau(dimensionVaisseau, positionVaisseau, Constante.VAISSEAU_VITESSE);
	}

	public Vaisseau recupererVaisseau() {
		return this.vaisseau;
	}

	public void tirerUnMissile(Dimension dimension, int vitesse) {
		this.missile = this.vaisseau.tirerUnMissile(dimension, vitesse);

	}
}
