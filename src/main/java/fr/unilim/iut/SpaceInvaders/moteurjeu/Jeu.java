package fr.unilim.iut.SpaceInvaders.moteurjeu;

/**
 * represente un jeu un jeu est caracterise par la methode evoluer a redefinir
 * 
 * @author Graou
 *
 */
public interface Jeu {

	public void evoluer(Commande commandeUser);
	
	/**
	 * @return true si et seulement si le jeu est fini
	 */
	public boolean etreFini();
}
