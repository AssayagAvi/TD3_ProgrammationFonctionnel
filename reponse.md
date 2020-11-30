# TD3 Réponses 

##Exercice 1 

###Question 1
Voir code

###Question 2 
Voir code

###Question 3 
Voici le code refactorer via l'API Stream sur l'exercice 4 du TD1

    public static Predicate<Produit> produitaTVAReduite = p -> p.cat().equals(Categorie.REDUIT);
    public static Predicate<Produit> sup5Euros = p -> p.prix() > 5.0;
    public static Predicate<Commande> doublon = cde -> cde.lignes().size() > 2;
    public static Predicate<Paire<Produit, Integer>> paireTVAReduite = paire -> produitaTVAReduite.test(paire.fst());

    public static Predicate<Paire<Produit, Integer>> genPredicate(int nombreMinAchats) {
        return p -> p.snd() > nombreMinAchats;
    }

    public static final Function<Paire<Produit, Integer>, Double> calcul1 =
            ligneDeCommande -> {
                final Produit produit = ligneDeCommande.fst();
                final double prix_unitaire = produit.prix();
                final double tva = produit.cat().tva();
                final int qte = ligneDeCommande.snd();
                return prix_unitaire * (1 + tva) * qte;
            };

    public static final Function<Paire<Produit, Integer>, Double> calcul2 =
            ligneDeCommande -> {
                final Produit produit = ligneDeCommande.fst();
                final double prix_unitaire = produit.prix();
                final double tva = produit.cat().tva();
                final int qte = ligneDeCommande.snd();
                if (qte > 2){
                    return calcul1.apply(ligneDeCommande) * 0.80;
                }else {
                    return calcul1.apply(ligneDeCommande);
                }
            };



    public static void q1(){
        DAO db = DAO.instance();
        //Produit tva reduite
        Set<Produit> v1 = db.selectionProduits(produitaTVAReduite);
        System.out.println(v1);
    }

    private static void q2(){
        DAO db = DAO.instance();
        //Produit tva reduite coutant plus de 5€
        Set<Produit> v2 = db.selectionProduits(produitaTVAReduite.and(sup5Euros));
        System.out.println(v2);
    }

    private static void q3(){
        DAO db = DAO.instance();
        //Commande non normalisé de + 2 item
        List<Commande> v3  = db.selectionCommande(doublon);
        System.out.println(v3);

    }
    private static void q4(){
        DAO db = DAO.instance();
    //commandes non normalisées 1 produit à TVA réduite commandé en + 2 exemplaire
        List<Commande> v4 = db.selectionCommandeSurExistanceLigne(
                paire -> paireTVAReduite.
                        and(genPredicate(2))
                        .test(paire)
        );
        System.out.println(v4);

    }

    private static void q5(){
        DAO db = DAO.instance();
        //afficher les commandes (sans to string)
        for (Commande cde : db.commandes()) {
            cde.affiche(calcul1);
        }
    }

    private static void q6(){
        DAO db = DAO.instance();
        //afficher les commandes (sans to string)
        for (Commande cde : db.commandes()) {
            cde.affiche(calcul2);
        }
    }