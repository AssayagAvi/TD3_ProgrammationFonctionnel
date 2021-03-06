package TD1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.ArrayList;

import static TD1.Categorie.INTERMEDIAIRE;
import static TD1.Categorie.NORMAL;
import static TD1.Categorie.REDUIT;



public class DAO {
    private List<Commande> commandes;

    private DAO(Commande c1, Commande... cs) {
        commandes = new ArrayList<>();
        commandes.add(c1);
        commandes.addAll(List.of(cs));
    }

    public static DAO instance = null;

    public static final DAO instance() {
        if (instance == null) {
            Produit camembert = new Produit("Camembert", 4.0, NORMAL);
            Produit yaourts = new Produit("Yaourts", 2.5, INTERMEDIAIRE);
            Produit masques = new Produit("Masques", 25.0, REDUIT);
            Produit gel = new Produit("Gel", 5.0, REDUIT);
            Produit tournevis = new Produit("Tournevis", 4.5, NORMAL);
            //
            Commande c1 = new Commande()
                    .ajouter(camembert, 1)
                    .ajouter(yaourts, 6);
            Commande c2 = new Commande()
                    .ajouter(masques, 2)
                    .ajouter(gel, 10)
                    .ajouter(camembert, 2)
                    .ajouter(masques, 3);
            //
            instance = new DAO(c1, c2);
        }
        return instance;
    }

    /**
     * liste de toutes les commandes
     */
    public List<Commande> commandes() {
        return commandes;
    }

    /**
     * ensemble des différents produits commandés
     */
    public Set<Produit> produits() {
        return commandes.stream()
                .flatMap(c -> c.lignes().stream())
                .map(Paire::fst)
                .collect(Collectors.toSet());
    }

    public Set<Produit> produitsNoStream() {

        Set<Produit> toutLesProduits = new HashSet<>();

        for (Commande c : commandes) {
            for (Paire<Produit, Integer> ligne : c.lignes()) {
                toutLesProduits.add(ligne.fst());
            }
        }

        return toutLesProduits;
    }

    /**
     * liste des commandes vérifiant un prédicat
     */
    public List<Commande> selectionCommande(Predicate<Commande> p) {
        return commandes.stream()
                .filter(p)
                .collect(Collectors.toList());
    }


    public List<Commande> selectionCommandeNoStream(Predicate<Commande> p){

    List<Commande> choixCommande = new ArrayList<>();
        for(Commande c : commandes) {
            if (p.test(c)) {
                choixCommande.add(c);
            }
        }
        return choixCommande;
}


    /**
     * liste des commandes dont au moins une ligne vérifie un prédicat
     */
    public List<Commande> selectionCommandeSurExistanceLigne(Predicate<Paire<Produit,Integer>> p) {
        return commandes.stream()
            .filter(c -> c.lignes().stream().anyMatch(p))
            .collect(Collectors.toList());
    }

    /**
     * ensemble des différents produits commandés vérifiant un prédicat
     */
    public Set<Produit> selectionProduits(Predicate<Produit> p) {
        return produits()
            .stream()
            .filter(p)
            .collect(Collectors.toSet());
    }
    public Set<Produit> selectionProduitsNoStream(Predicate<Produit> condition) {
        Set<Produit> choixProduits = new HashSet<>();
        for(Produit p : produits()) {
            if(condition.test(p)) {
                choixProduits.add(p);
            }
        }
        return choixProduits;
    }
}


