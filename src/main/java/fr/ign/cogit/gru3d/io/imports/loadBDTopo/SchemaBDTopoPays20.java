package fr.ign.cogit.gru3d.io.imports.loadBDTopo;

import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.type.FC_FeatureAttributeValue;
import fr.ign.cogit.geoxygene.api.feature.type.GF_AttributeType;
import fr.ign.cogit.geoxygene.schema.schemaConceptuelISOProduit.AttributeType;
import fr.ign.cogit.geoxygene.schema.schemaConceptuelISOProduit.FeatureType;
import fr.ign.cogit.geoxygene.schema.schemaConceptuelISOProduit.SchemaConceptuelProduit;

public class SchemaBDTopoPays20 {

  public static SchemaConceptuelProduit creeSchemaBDTopoPays20() {

    /***************************************************************************
     * Creation du catalogue de la base de données
     * *************************************************************************/
    SchemaConceptuelProduit sProduit = new SchemaConceptuelProduit();
    sProduit.setNomSchema("Catalogue BDTOPO");
    sProduit.setBD("BDTOPO V2.0");
    sProduit.setTagBD(1);
    sProduit.setDate("Février 2008");
    sProduit.setVersion("2.0");
    sProduit.setSource("Photogrammétrie");
    sProduit.setSujet("Composante topographique du RGE");

    /***************************************************************************
     * Ajout du thème réseau routier
     * *************************************************************************/

    // Classe Route///////////////////////////////////////////////////

    sProduit.createFeatureType("ROUTE");
    FeatureType route = (FeatureType) (sProduit.getFeatureTypeByName("ROUTE"));
    route
        .setDefinition("Portion de voie de communication destinée aux automobiles, aux piétons, aux cycles ou aux animaux, homogène pour l'ensemble des attributs et des relations qui la concernent. Le tronçon de route peut être revêtu ou non revêtu (pas de revêtement de surface ou revêtement de surface fortement dégradé). Dans le cas d'un tronçon de route revêtu, on représente uniquement la chaussée, délimitée par les bas-côtés ou les trottoirs.");
    route.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(route, "ID", "string", false);
    AttributeType id1 = route.getFeatureAttributeByName("ID");
    id1.setDefinition("Identifiant Tronçon.Cet identifiant est unique. Il est stable d’une édition à l’autre. Il permet aussi d’établir un lien entre le ponctuel de la classe « ADRESSE » des produits BD ADRESSE® et POINT ADRESSE® (par l’intermédiaire de l’attribut ID_TR) et l’objet linéaire de la classe « ROUTE ».");

    // Attribut PREC_PLANI
    sProduit.createFeatureAttribute(route, "PREC_PLANI", "float", true);
    AttributeType prec_plani1 = route.getFeatureAttributeByName("PREC_PLANI");
    prec_plani1
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani1, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani1, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani1, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani1, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani1, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani1, "30.0");

    // Attribut PREC_ALTI
    sProduit.createFeatureAttribute(route, "PREC_ALTI", "float", true);
    AttributeType prec_alti1 = route.getFeatureAttributeByName("PREC_ALTI");
    prec_alti1
        .setDefinition("Précision géométrique altimétrique. Attribut précisant la précision géométrique en altimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_alti1, "1.0");
    sProduit.createFeatureAttributeValue(prec_alti1, "2.5");
    sProduit.createFeatureAttributeValue(prec_alti1, "20.0");
    sProduit.createFeatureAttributeValue(prec_alti1, "9999.0");

    // Attribut NATURE
    sProduit.createFeatureAttribute(route, "NATURE", "string", true);
    AttributeType nature1 = route.getFeatureAttributeByName("NATURE");
    nature1
        .setDefinition("Attribut permettant de distinguer différentes natures de tronçon de route.");
    sProduit.createFeatureAttributeValue(nature1, "Autoroute");
    FC_FeatureAttributeValue autoroute = nature1
        .getFeatureAttributeValueByName("Autoroute");
    autoroute
        .setDefinition("Routes sans croisement, accessibles seulement en des points aménagés à cet effet et réservées aux véhicules à propulsion mécanique. Le classement dans la catégorie des autoroutes est prononcé par décret du conseil d’état, pris après enquête publique.");
    sProduit.createFeatureAttributeValue(nature1, "Quasi-autoroute");
    FC_FeatureAttributeValue quasiautoroute = nature1
        .getFeatureAttributeValueByName("Quasi-autoroute");
    quasiautoroute
        .setDefinition("Routes de même définition que l’autoroute mais non classées officiellement dans cette catégorie. Ce sont des routes à chaussées séparées par un terre-plein central, qui ne possèdent pas de croisement à niveau avec le reste du réseau routier.");
    sProduit.createFeatureAttributeValue(nature1, "Bretelle");
    FC_FeatureAttributeValue bretelle = nature1
        .getFeatureAttributeValueByName("Bretelle");
    bretelle
        .setDefinition("Bretelles de liaison (ou d’échangeur) ou voies d’accès à une Aire de Service ou de Repos.");
    sProduit.createFeatureAttributeValue(nature1, "Route à 2 chaussées");
    FC_FeatureAttributeValue routeA2Chaussees = nature1
        .getFeatureAttributeValueByName("Route à 2 chaussées");
    routeA2Chaussees
        .setDefinition("Routes comportant 2 chaussées séparées par un obstacle physique éventuellement ouvert aux carrefours. Elles possèdent donc des croisements à niveau, ce qui leur interdit d'être classées dans la catégorie Autoroute ou Quasi-autoroute.");
    sProduit.createFeatureAttributeValue(nature1, "Route à 1 chaussée");
    FC_FeatureAttributeValue routeA1Chaussee = nature1
        .getFeatureAttributeValueByName("Route à 1 chaussée");
    routeA1Chaussee
        .setDefinition("Routes comportant 1 chaussée.Toutes les routes goudronnées qui ne sont pas classées en Route à 2 chaussées, Quasi-autoroute ou Autoroute se retrouvent dans cette classe.");
    sProduit.createFeatureAttributeValue(nature1, "Route empierrée");
    FC_FeatureAttributeValue routeEmpierree = nature1
        .getFeatureAttributeValueByName("Route empierrée");
    routeEmpierree
        .setDefinition("Routes sommairement revêtues (pas de revêtement de surface ou revêtement très dégradé), mais permettant la circulation de véhicules automobiles de tourisme par tout temps. Toutes les routes empierrées sont incluses.");
    sProduit.createFeatureAttributeValue(nature1, "Chemin");
    FC_FeatureAttributeValue chemin = nature1
        .getFeatureAttributeValueByName("Chemin");
    chemin
        .setDefinition("Les chemins sont prévus pour la circulation de véhicules ou d’engins d’exploitation. Ils ne sont pas forcément carrossables pour tous les véhicules et par tout temps (voir aussi “route empierrée”).");
    sProduit.createFeatureAttributeValue(nature1, "Bac auto");
    FC_FeatureAttributeValue bacAuto = nature1
        .getFeatureAttributeValueByName("Bac auto");
    bacAuto
        .setDefinition("Trajets du bateau servant à passer des véhicules d’une rive à l’autre d’un cours d’eau ou d’un bras de mer.");
    sProduit.createFeatureAttributeValue(nature1, "Bac piéton");
    FC_FeatureAttributeValue bacPieton = nature1
        .getFeatureAttributeValueByName("Bac piéton");
    bacPieton
        .setDefinition("Trajets du bateau servant à passer des piétons d’une rive à l’autre d’un cours d’eau ou d’un bras de mer.");
    sProduit.createFeatureAttributeValue(nature1, "Piste cyclable");
    FC_FeatureAttributeValue pisteCyclable = nature1
        .getFeatureAttributeValueByName("Piste cyclable");
    pisteCyclable
        .setDefinition("Tronçons de chaussée en site propre, réservée aux cycles et cyclomoteurs. La longueur doit être supérieure à 200m. Les bandes cyclables sont exclues.");
    sProduit.createFeatureAttributeValue(nature1, "Sentier");
    FC_FeatureAttributeValue sentier = nature1
        .getFeatureAttributeValueByName("Sentier");
    sentier
        .setDefinition("Chemins étroits ne permettant pas le passage de véhicules. Seuls les principaux sentiers sont inclus. Passerelles supportant une allée, directement reliés au réseau routier. Les passerelles ont une position par rapport au sol supérieure à 0.");
    sProduit.createFeatureAttributeValue(nature1, "Escalier");
    FC_FeatureAttributeValue escalier = nature1
        .getFeatureAttributeValueByName("Escalier");
    escalier
        .setDefinition("Escaliers directement reliés au réseau routier supportant une allée, assurant la jonction entre deux voies de communication ou entre le réseau routier et un élément adressable. Sur rue, les escaliers visibles sur les photographies aériennes sont distingués quelle que soit leur longueur.");

    // Attribut NUMERO
    sProduit.createFeatureAttribute(route, "NUMERO", "string", false);
    AttributeType numero1 = route.getFeatureAttributeByName("NUMERO");
    numero1
        .setDefinition("Désigne le classement administratif d'un tronçon routier.");
    // sProduit.createFeatureAttributeValue(numero1, "NC");
    // FC_FeatureAttributeValue nc1 =
    // numero1.getFeatureAttributeValueByName("NC");
    // nc1.setDefinition("Non concerné : l’attribut « NUMERO » n’a pas lieu d’être renseigné (cas d’un chemin par exemple).");
    // sProduit.createFeatureAttributeValue(numero1, "NR");
    // FC_FeatureAttributeValue nr1 =
    // numero1.getFeatureAttributeValueByName("NR");
    // nc1.setDefinition("Non renseigné : même si le numéro n’est pas connu, le tronçon en porte peut-être un. L’information est manquante dans le produit.");

    // Attribut NOM_RUE_G
    sProduit.createFeatureAttribute(route, "NOM_RUE_G", "string", false);
    AttributeType nomRueG1 = route.getFeatureAttributeByName("NOM_RUE_G");
    nomRueG1
        .setDefinition("Nom rue gauche. Une rue est un ensemble de tronçons de route associés à un même nom. Une rue est identifiée par son nom dans une commune donnée. « NOM_RUE_G » est le nom porté par la rue pour la commune dont le numéro INSEE est identique à la valeur de l’attribut « INSEECOM_G ». Dans les résidences portant un nom avec des allées comportant également des noms, on garde le nom servant à l’adressage. Dans certains cas le toponyme qui sert à l’adressage est un nom de lieu-dit. Il servira à nommer la voie qui dessert ces adresses. Les lettres « LD » placées devant le toponyme serviront à les identifier.");

    // Attribut NOM_RUE_D
    sProduit.createFeatureAttribute(route, "NOM_RUE_D", "string", false);
    AttributeType nomRueD1 = route.getFeatureAttributeByName("NOM_RUE_D");
    nomRueD1
        .setDefinition("Nom rue droite. Une rue est un ensemble de tronçons de route associés à un même nom. Une rue est identifiée par son nom dans une commune donnée. « NOM_RUE_D » est le nom porté par la rue pour la commune dont le numéro INSEE est identique à la valeur de l’attribut « INSEECOM_D ». Dans les résidences portant un nom avec des allées comportant également des noms, on garde le nom servant à l’adressage. Dans certains cas le toponyme qui sert à l’adressage est un nom de lieu-dit. Il servira à nommer la voie qui dessert ces adresses. Les lettres « LD » placées devant le toponyme serviront à les identifier.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(route, "IMPORTANCE", "string", true);
    AttributeType importance1 = route.getFeatureAttributeByName("IMPORTANCE");
    importance1
        .setDefinition("Cet attribut matérialise une hiérarchisation du réseau routier fondée, non pas sur un critère administratif, mais sur l'importance des tronçons de route pour le trafic routier. Ainsi, les valeurs \"1\", \"2\", \"3\", \"4\", \"5\" permettent un maillage de plus en plus dense du territoire. Le graphe des éléments appartenant à un degré (autre que le plus bas) et aux niveaux supérieurs est connexe.");
    sProduit.createFeatureAttributeValue(importance1, "1");
    FC_FeatureAttributeValue un1 = importance1
        .getFeatureAttributeValueByName("1");
    un1.setDefinition("Le réseau 1 assure les liaisons entre métropoles et compose l’essentiel du réseau européen. Il est composé en général d’autoroutes et quasi-autoroutes, parfois de nationales.");
    sProduit.createFeatureAttributeValue(importance1, "2");
    FC_FeatureAttributeValue deux1 = importance1
        .getFeatureAttributeValueByName("2");
    deux1
        .setDefinition("Liaisons entre départements. Cette valeur représente une densification du maillage routier défini par les tronçons d’importance 1. Les liaisons d’importance 2 ont fonction d’assurer les liaisons à fort trafic à caractère prioritaire entre agglomérations importantes, d’assurer les liaisons des agglomérations importantes au réseau d’importance 1, d’offrir une alternative à une autoroute si celle-ci est payante, de proposer des itinéraires de contournement des agglomérations, d’assurer la continuité, en agglomération, des liaisons interurbaines à fort trafic quand il n’y a pas de contournement possible.");
    sProduit.createFeatureAttributeValue(importance1, "3");
    FC_FeatureAttributeValue trois1 = importance1
        .getFeatureAttributeValueByName("3");
    trois1
        .setDefinition("Liaisons ville à ville à l’intérieur d’un département. Ce niveau est majoritairement représenté par des routes départementales, toutefois certaines départementales peuvent avoir une importance 4 ou 5. Cette valeur représente une densification du maillage routier défini par les tronçons d’importance 2. Les liaisons d’importance 3 ont fonction de relier les communes de moindre importance entre elles (les chefs-lieux de canton en particulier), de desservir les localités et sites touristiques importants, de desservir les points de passage des obstacles naturels quand ils sont peu nombreux (cols routiers, ponts), de desservir les agglomérations d'où partent des liaisons maritimes, de structurer la circulation en agglomération.");
    sProduit.createFeatureAttributeValue(importance1, "4");
    FC_FeatureAttributeValue quatre1 = importance1
        .getFeatureAttributeValueByName("4");
    quatre1
        .setDefinition("Voies permettant de se déplacer rapidement à l’intérieur d’une commune et, dans les zones rurales, de relier le bourg aux hameaux proches. Cette valeur représente une densification du maillage routier défini par les tronçons d’importance 3. Les liaisons d’importance 4 ont fonction de structurer la circulation en agglomération, de relier le bourg aux hameaux proches.");
    sProduit.createFeatureAttributeValue(importance1, "5");
    FC_FeatureAttributeValue cinq1 = importance1
        .getFeatureAttributeValueByName("5");
    cinq1
        .setDefinition("Voies permettant de desservir l’intérieur d’une commune. Valeur prise par exclusion des autres valeurs de l'attribut.");
    sProduit.createFeatureAttributeValue(importance1, "NC");
    FC_FeatureAttributeValue nc1bis = importance1
        .getFeatureAttributeValueByName("NC");
    nc1bis.setDefinition("Non concerné par cet attribut.");
    sProduit.createFeatureAttributeValue(importance1, "NR");
    FC_FeatureAttributeValue nr1bis = importance1
        .getFeatureAttributeValueByName("NR");
    nr1bis.setDefinition("Non renseigné.");

    // Attribut CL_ADMIN
    sProduit.createFeatureAttribute(route, "CL_ADMIN", "string", true);
    AttributeType clAdmin1 = route.getFeatureAttributeByName("CL_ADMIN");
    clAdmin1
        .setDefinition("Classement administratif. Attribut précisant le statut d'une route numérotée ou nommée.");
    sProduit.createFeatureAttributeValue(clAdmin1, "Autoroute");
    FC_FeatureAttributeValue autoroute1 = clAdmin1
        .getFeatureAttributeValueByName("Autoroute");
    autoroute1
        .setDefinition("Les autoroutes sont des routes sans croisement, accessibles seulement en des points aménagés à cet effet et réservées aux véhicules à propulsion mécanique (Article L122-1 du code de la voirie routière).");
    sProduit.createFeatureAttributeValue(clAdmin1, "Nationale");
    FC_FeatureAttributeValue nationale1 = clAdmin1
        .getFeatureAttributeValueByName("Nationale");
    nationale1
        .setDefinition("Route nationale (voies du domaine public routier national autres que les autoroutes précédemment définies).");
    sProduit.createFeatureAttributeValue(clAdmin1, "Départementale");
    FC_FeatureAttributeValue departementale1 = clAdmin1
        .getFeatureAttributeValueByName("Départementale");
    departementale1
        .setDefinition("Voie qui fait partie du domaine public routier départemental.");
    sProduit.createFeatureAttributeValue(clAdmin1, "Autre");
    FC_FeatureAttributeValue autre1 = clAdmin1
        .getFeatureAttributeValueByName("Autre");
    autre1
        .setDefinition("Toute autre voie non classée dans les catégories administratives précédentes.");

    // Attribut GESTION
    sProduit.createFeatureAttribute(route, "GESTION", "string", false);
    AttributeType gestion1 = route.getFeatureAttributeByName("GESTION");
    gestion1
        .setDefinition("Gestionnaire. Définit le gestionnaire administratif d’une route. Toutes les routes classées possèdent un ‘Gestionnaire’. Il existe différentes catégories de routes pour lesquelles le gestionnaire diffère : pour les routes départementales, il s’agit du gestionnaire départemental de la route au sens administratif (c’est-à-dire le numéro de département),  pour les routes nationales et les autoroutes non concédées, le gestionnaire correspond également au gestionnaire départemental de la route (au sens administratif), pour les autoroutes concédées, le gestionnaire est la société concessionnaire d’autoroute (une correspondance est établie entre ces sociétés et un code en trois lettres), les routes codées sur les bretelles d’échangeurs identifiés prennent le gestionnaire de la route à laquelle l’échangeur est rattaché.");
    // Serait complétable au niveau des valeurs énumérées moyennant beaucoup
    // de
    // patience

    // Attribut MISE_SERV
    sProduit.createFeatureAttribute(route, "MISE_SERV", "string", false);
    AttributeType miseServ1 = route.getFeatureAttributeByName("MISE_SERV");
    miseServ1
        .setDefinition("Date de mise en service. Définit la date prévue ou la date effective de mise en service d’un tronçon de route. Cet attribut n'est rempli que pour les tronçons en construction, il est à “1000-01-01“ dans les autres cas. Les tronçons qui possèdent une date de mise en service sont complètement fermés aux véhicules avant cette date.");

    // Attribut IT_VERT
    sProduit.createFeatureAttribute(route, "IT_VERT", "string", false);
    AttributeType itVert1 = route.getFeatureAttributeByName("IT_VERT");
    itVert1
        .setDefinition("Itinéraire vert. Indique l’appartenance ou non d’un tronçon routier au réseau vert. Le réseau vert, composé de pôles verts et de liaisons vertes, couvre l’ensemble du territoire français.Les pôles verts sont composés de communes de plus de 23.000 habitants en province et de 39.000 habitants en Ile-de-France, ainsi que certains pôles d’activités administratifs, économiques, touristiques ou industriels. On retient seulement le réseau vert de transit entre pôles verts. Le réseau vert de rabattement, à l’intérieur des villes, et le réseau vert conseillé aux poids lourds ne sont pas retenus.");

    // Attribut IT_EUROP
    sProduit.createFeatureAttribute(route, "IT_EUROP", "string", false);
    AttributeType itEurop1 = route.getFeatureAttributeByName("IT_EUROP");
    itEurop1
        .setDefinition("Itinéraire européen. Numéro de route européenne : une route européenne emprunte en général le réseau autoroutier ou national (exceptionnellement départemental ou non classé).");

    // Attribut FICTIF
    sProduit.createFeatureAttribute(route, "FICTIF", "string", false);
    AttributeType fictif1 = route.getFeatureAttributeByName("FICTIF");
    fictif1
        .setDefinition("La valeur “oui“ indique que la géométrie du tronçon de route n'est pas significative. La présence de ce dernier sert à raccorder une bretelle à l’axe d’une chaussée afin d'assurer la continuité du réseau routier linéaire.");
    sProduit.createFeatureAttributeValue(fictif1, "Oui");
    sProduit.createFeatureAttributeValue(fictif1, "Non");

    // Attribut FRANCHISSMT
    sProduit.createFeatureAttribute(route, "FRANCHISSMT", "string", true);
    AttributeType franchissmt1 = route.getFeatureAttributeByName("FRANCHISSMT");
    franchissmt1
        .setDefinition("Franchissement.Cet attribut informe sur le niveau de l’objet par rapport à la surface du sol.");
    sProduit.createFeatureAttributeValue(franchissmt1, "Gué ou radier");
    FC_FeatureAttributeValue gue1 = franchissmt1
        .getFeatureAttributeValueByName("Gué ou radier");
    gue1.setDefinition("Passage naturel ou aménagé permettant aux véhicules de traverser un cours d’eau sans le recours d’un pont ou d’un bateau.");
    sProduit.createFeatureAttributeValue(franchissmt1, "Pont");
    FC_FeatureAttributeValue pont1 = franchissmt1
        .getFeatureAttributeValueByName("Pont");
    pont1
        .setDefinition("Tronçon de route situé au-dessus du niveau du sol (Ponceau, Pont, Pont mobile, Viaduc, Passerelle).");
    sProduit.createFeatureAttributeValue(franchissmt1, "Tunnel");
    FC_FeatureAttributeValue tunnel1 = franchissmt1
        .getFeatureAttributeValueByName("Tunnel");
    tunnel1
        .setDefinition("Tronçon de route situé sous le niveau du sol (Tunnel).");
    sProduit.createFeatureAttributeValue(franchissmt1, "NC");
    FC_FeatureAttributeValue nc1 = franchissmt1
        .getFeatureAttributeValueByName("NC");
    nc1.setDefinition("Tronçon de route situé au niveau du sol (y compris les tronçons en déblai et en remblai).");

    // Attribut Largeur
    sProduit.createFeatureAttribute(route, "LARGEUR", "float", false);
    AttributeType largeur1 = route.getFeatureAttributeByName("LARGEUR");
    largeur1
        .setDefinition("Largeur de chaussée. Largeur de chaussée (d’accotement à accotement) exprimée en mètres.");

    // Attribut NOM_ITI
    sProduit.createFeatureAttribute(route, "NOM_ITI", "string", false);
    AttributeType nomIti1 = route.getFeatureAttributeByName("NOM_ITI");
    nomIti1
        .setDefinition("Nom d’itinéraire. Définit un parcours routier nommé.");

    // Attribut NB_VOIES
    sProduit.createFeatureAttribute(route, "NB_VOIES", "integer", false);
    AttributeType nbVoies1 = route.getFeatureAttributeByName("NB_VOIES");
    nbVoies1
        .setDefinition("Nombre de voies.Nombre total de voies d’une route, d’une rue ou d’une chaussée de route à chaussées séparées.Lorsque les voies ne sont pas matérialisées, l’attribut indique le nombre maximum de voies de circulation effectivement utilisées dans des conditions normales de circulation.L'augmentation du nombre de voies au niveau d'un carrefour pour permettre de tourner plus facilement à droite ou à gauche n'est pas prise en compte, ainsi que les voies d'accélération ou de décélération des échangeurs d'autoroute.");

    // Attribut POS_SOL
    sProduit.createFeatureAttribute(route, "POS_SOL", "integer", false);
    AttributeType posSol1 = route.getFeatureAttributeByName("POS_SOL");
    posSol1
        .setDefinition("Position par rapport au sol. Donne le niveau de l’objet par rapport à la surface du sol (valeur négative pour un objet souterrain, nulle pour un objet au sol et positive pour un objet en sursol). Si l’objet en sursol passe au dessus d’autres objets en sursol, sa valeur « position par rapport au sol » est égale à « 1 + le nombre d’objets intercalés ». De la même façon, un souterrain peut prendre une valeur « position par rapport au sol » égale à « – 1 – le nombre d’objets souterrains intercalés ».");

    // Attribut SENS
    sProduit.createFeatureAttribute(route, "SENS", "string", true);
    AttributeType sens1 = route.getFeatureAttributeByName("SENS");
    sens1
        .setDefinition("Sens de circulation autorisée pour les automobiles sur les voies.");
    sProduit.createFeatureAttributeValue(sens1, "Double");
    FC_FeatureAttributeValue double1 = sens1
        .getFeatureAttributeValueByName("Double");
    double1.setDefinition("La circulation est autorisée dans les deux sens.");
    sProduit.createFeatureAttributeValue(sens1, "Direct");
    FC_FeatureAttributeValue direct1 = sens1
        .getFeatureAttributeValueByName("Direct");
    direct1
        .setDefinition("La circulation n’est autorisée que dans le sens de numérisation du tronçon.");
    sProduit.createFeatureAttributeValue(sens1, "Inverse");
    FC_FeatureAttributeValue inverse1 = sens1
        .getFeatureAttributeValueByName("Inverse");
    inverse1
        .setDefinition("La circulation n’est autorisée que dans le sens inverse de numérisation du tronçon.");
    sProduit.createFeatureAttributeValue(sens1, "NC");
    FC_FeatureAttributeValue nc2 = sens1.getFeatureAttributeValueByName("NC");
    nc2.setDefinition("Non concerné.");
    sProduit.createFeatureAttributeValue(sens1, "NR");
    FC_FeatureAttributeValue nr2 = sens1.getFeatureAttributeValueByName("NR");
    nr2.setDefinition("Non renseigné : l’information est manquante dans la base.");

    // Attribut INSEECOM_G
    sProduit.createFeatureAttribute(route, "INSEECOM_G", "string", false);
    AttributeType inseecom_g1 = route.getFeatureAttributeByName("INSEECOM_G");
    inseecom_g1
        .setDefinition("INSEE Commune gauche. Numéro d’INSEE de la commune à gauche du tronçon par rapport à son sens de numérisation.");

    // Attribut INSEECOM_D
    sProduit.createFeatureAttribute(route, "INSEECOM_D", "string", false);
    AttributeType inseecom_d1 = route.getFeatureAttributeByName("INSEECOM_D");
    inseecom_d1
        .setDefinition("INSEE Commune droite. Numéro d’INSEE de la commune à droite du tronçon par rapport à son sens de numérisation.");

    // Attribut CODEVOIE_G
    sProduit.createFeatureAttribute(route, "CODEVOIE_G", "string", false);
    AttributeType codevoie_g1 = route.getFeatureAttributeByName("CODEVOIE_G");
    codevoie_g1
        .setDefinition("Identifiant gauche. Identifiant de la voie associée au côté gauche du tronçon.Il est obtenu par concaténation du numéro d’INSEE (5 caractères) et du code Rivoli (4 caractères) de la voie associée au côté gauche du tronçon.");

    // Attribut CODEVOIE_D
    sProduit.createFeatureAttribute(route, "CODEVOIE_D", "string", false);
    AttributeType codevoie_d1 = route.getFeatureAttributeByName("CODEVOIE_D");
    codevoie_d1
        .setDefinition("Identifiant droite. Identifiant de la voie associée au côté droit du tronçon.Il est obtenu par concaténation du numéro d’INSEE (5 caractères) et du code Rivoli (4 caractères) de la voie associée au côté droit du tronçon.");

    // Attribut TYP_ADRES
    sProduit.createFeatureAttribute(route, "TYP_ADRES", "string", true);
    AttributeType typAdres1 = route.getFeatureAttributeByName("TYP_ADRES");
    typAdres1
        .setDefinition("Type d’adressage. Renseigne sur le type d’adressage du tronçon.");
    sProduit.createFeatureAttributeValue(typAdres1, "Classique");
    FC_FeatureAttributeValue clasic1 = typAdres1
        .getFeatureAttributeValueByName("Classique");
    clasic1
        .setDefinition("Un côté de la rue porte des numéros pairs, l’autre des numéros impairs. Les numéros sont ordonnés par ordre croissant ou décroissant le long de la rue.");
    sProduit.createFeatureAttributeValue(typAdres1, "Métrique");
    FC_FeatureAttributeValue metric1 = typAdres1
        .getFeatureAttributeValueByName("Métrique");
    metric1
        .setDefinition("Les numéros des bornes postales correspondent à la distance en mètres qui sépare l’entrée principale de la parcelle d’un point origine arbitraire de la rue. Le principe de côté pair et impair n’est pas toujours conservé.");
    sProduit.createFeatureAttributeValue(typAdres1, "Linéaire");
    FC_FeatureAttributeValue lineaire1 = typAdres1
        .getFeatureAttributeValueByName("Linéaire");
    lineaire1
        .setDefinition("Les numéros sont ordonnés le long de chaque côté de la rue, mais sans distinction pair ou impair.");
    sProduit.createFeatureAttributeValue(typAdres1, "Autre");
    FC_FeatureAttributeValue autre2 = typAdres1
        .getFeatureAttributeValueByName("Autre");
    autre2
        .setDefinition("Ni classique, ni métrique, ni linéaire. Les numéros ne sont pas ordonnés.");
    sProduit.createFeatureAttributeValue(typAdres1, "NC");
    FC_FeatureAttributeValue nc3 = typAdres1
        .getFeatureAttributeValueByName("NC");
    nc3.setDefinition("Non concerné.");
    sProduit.createFeatureAttributeValue(typAdres1, "NR");
    FC_FeatureAttributeValue nr3 = typAdres1
        .getFeatureAttributeValueByName("NR");
    nr3.setDefinition("Non renseigné.");

    // Attribut BORNEDEB_G
    sProduit.createFeatureAttribute(route, "BORNEDEB_G", "integer", false);
    AttributeType bornedeb_g1 = route.getFeatureAttributeByName("BORNEDEB_G");
    bornedeb_g1
        .setDefinition("Borne début gauche. Numéro de borne à gauche du tronçon en son sommet initial.");

    // Attribut BORNEDEB_D
    sProduit.createFeatureAttribute(route, "BORNEDEB_D", "integer", false);
    AttributeType bornedeb_d1 = route.getFeatureAttributeByName("BORNEDEB_D");
    bornedeb_d1
        .setDefinition("Borne début droite. Numéro de borne à droite du tronçon en son sommet initial.");

    // Attribut BORNEFIN_G
    sProduit.createFeatureAttribute(route, "BORNEFIN_G", "integer", false);
    AttributeType bornefin_g1 = route.getFeatureAttributeByName("BORNEFIN_G");
    bornefin_g1
        .setDefinition("Borne fin gauche. Numéro de borne à gauche du tronçon en son sommet final.");

    // Attribut BORNEFIN_D
    sProduit.createFeatureAttribute(route, "BORNEFIN_D", "integer", false);
    AttributeType bornefin_d1 = route.getFeatureAttributeByName("BORNEFIN_D");
    bornefin_d1
        .setDefinition("Borne fin droite. Numéro de borne à droite du tronçon en son sommet final.");

    // Attribut ETAT
    sProduit.createFeatureAttribute(route, "ETAT", "string", false);
    AttributeType etat1 = route.getFeatureAttributeByName("ETAT");
    etat1.setDefinition("Etat du tronçon.");

    // Attribut Z_INI
    sProduit.createFeatureAttribute(route, "Z_INI", "float", false);
    AttributeType zini1 = route.getFeatureAttributeByName("Z_INI");
    zini1
        .setDefinition("Altitude initiale : c’est l’altitude du sommet initial du tronçon.");

    // Attribut Z_FIN
    sProduit.createFeatureAttribute(route, "Z_FIN", "float", false);
    AttributeType zfin1 = route.getFeatureAttributeByName("Z_FIN");
    zfin1
        .setDefinition("Altitude finale : c’est l’altitude du sommet final du tronçon.");

    // Classe Route
    // nommee///////////////////////////////////////////////////

    sProduit.createFeatureType("ROUTE_NOMMEE");
    FeatureType routeNommee = (FeatureType) (sProduit
        .getFeatureTypeByName("ROUTE_NOMMEE"));
    routeNommee
        .setDefinition("Portion de voie de communication destinée aux automobiles, aux piétons, aux cycles ou aux animaux, homogène pour l'ensemble des attributs et des relations qui la concernent. Le tronçon de route peut être revêtu ou non revêtu (pas de revêtement de surface ou revêtement de surface fortement dégradé). Dans le cas d'un tronçon de route revêtu, on représente uniquement la chaussée, délimitée par les bas-côtés ou les trottoirs.");
    routeNommee.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(routeNommee, "ID", "string", false);
    AttributeType id2 = routeNommee.getFeatureAttributeByName("ID");
    id2.setDefinition("Identifiant Tronçon.Cet identifiant est unique. Il est stable d’une édition à l’autre. Il permet aussi d’établir un lien entre le ponctuel de la classe « ADRESSE » des produits BD ADRESSE® et POINT ADRESSE® (par l’intermédiaire de l’attribut ID_TR) et l’objet linéaire de la classe « ROUTE ».");

    // Attribut PREC_PLANI
    sProduit.createFeatureAttribute(routeNommee, "PREC_PLANI", "float", true);
    AttributeType prec_plani2 = routeNommee
        .getFeatureAttributeByName("PREC_PLANI");
    prec_plani2
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani2, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani2, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani2, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani2, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani2, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani2, "30.0");

    // Attribut PREC_ALTI
    sProduit.createFeatureAttribute(routeNommee, "PREC_ALTI", "float", true);
    AttributeType prec_alti2 = routeNommee
        .getFeatureAttributeByName("PREC_ALTI");
    prec_alti2
        .setDefinition("Précision géométrique altimétrique. Attribut précisant la précision géométrique en altimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_alti2, "1.0");
    sProduit.createFeatureAttributeValue(prec_alti2, "2.5");
    sProduit.createFeatureAttributeValue(prec_alti2, "20.0");
    sProduit.createFeatureAttributeValue(prec_alti2, "9999.0");

    // Attribut NATURE
    sProduit.createFeatureAttribute(routeNommee, "NATURE", "string", true);
    AttributeType nature2 = routeNommee.getFeatureAttributeByName("NATURE");
    nature2
        .setDefinition("Attribut permettant de distinguer différentes natures de tronçon de route.");
    sProduit.createFeatureAttributeValue(nature2, "Autoroute");
    FC_FeatureAttributeValue autoroute2 = nature2
        .getFeatureAttributeValueByName("Autoroute");
    autoroute2
        .setDefinition("Routes sans croisement, accessibles seulement en des points aménagés à cet effet et réservées aux véhicules à propulsion mécanique. Le classement dans la catégorie des autoroutes est prononcé par décret du conseil d’état, pris après enquête publique.");
    sProduit.createFeatureAttributeValue(nature2, "Quasi-autoroute");
    FC_FeatureAttributeValue quasiautoroute2 = nature2
        .getFeatureAttributeValueByName("Quasi-autoroute");
    quasiautoroute2
        .setDefinition("Routes de même définition que l’autoroute mais non classées officiellement dans cette catégorie. Ce sont des routes à chaussées séparées par un terre-plein central, qui ne possèdent pas de croisement à niveau avec le reste du réseau routier.");
    sProduit.createFeatureAttributeValue(nature2, "Bretelle");
    FC_FeatureAttributeValue bretelle2 = nature2
        .getFeatureAttributeValueByName("Bretelle");
    bretelle2
        .setDefinition("Bretelles de liaison (ou d’échangeur) ou voies d’accès à une Aire de Service ou de Repos.");
    sProduit.createFeatureAttributeValue(nature2, "Route à 2 chaussées");
    FC_FeatureAttributeValue routeA2Chaussees2 = nature2
        .getFeatureAttributeValueByName("Route à 2 chaussées");
    routeA2Chaussees2
        .setDefinition("Routes comportant 2 chaussées séparées par un obstacle physique éventuellement ouvert aux carrefours. Elles possèdent donc des croisements à niveau, ce qui leur interdit d'être classées dans la catégorie Autoroute ou Quasi-autoroute.");
    sProduit.createFeatureAttributeValue(nature2, "Route à 1 chaussée");
    FC_FeatureAttributeValue routeA1Chaussee2 = nature2
        .getFeatureAttributeValueByName("Route à 1 chaussée");
    routeA1Chaussee2
        .setDefinition("Routes comportant 1 chaussée.Toutes les routes goudronnées qui ne sont pas classées en Route à 2 chaussées, Quasi-autoroute ou Autoroute se retrouvent dans cette classe.");
    sProduit.createFeatureAttributeValue(nature2, "Route empierrée");
    FC_FeatureAttributeValue routeEmpierree2 = nature2
        .getFeatureAttributeValueByName("Route empierrée");
    routeEmpierree2
        .setDefinition("Routes sommairement revêtues (pas de revêtement de surface ou revêtement très dégradé), mais permettant la circulation de véhicules automobiles de tourisme par tout temps. Toutes les routes empierrées sont incluses.");
    sProduit.createFeatureAttributeValue(nature2, "Chemin");
    FC_FeatureAttributeValue chemin2 = nature2
        .getFeatureAttributeValueByName("Chemin");
    chemin2
        .setDefinition("Les chemins sont prévus pour la circulation de véhicules ou d’engins d’exploitation. Ils ne sont pas forcément carrossables pour tous les véhicules et par tout temps (voir aussi “route empierrée”).");
    sProduit.createFeatureAttributeValue(nature2, "Bac auto");
    FC_FeatureAttributeValue bacAuto2 = nature2
        .getFeatureAttributeValueByName("Bac auto");
    bacAuto2
        .setDefinition("Trajets du bateau servant à passer des véhicules d’une rive à l’autre d’un cours d’eau ou d’un bras de mer.");
    sProduit.createFeatureAttributeValue(nature2, "Bac piéton");
    FC_FeatureAttributeValue bacPieton2 = nature2
        .getFeatureAttributeValueByName("Bac piéton");
    bacPieton2
        .setDefinition("Trajets du bateau servant à passer des piétons d’une rive à l’autre d’un cours d’eau ou d’un bras de mer.");
    sProduit.createFeatureAttributeValue(nature2, "Piste cyclable");
    FC_FeatureAttributeValue pisteCyclable2 = nature2
        .getFeatureAttributeValueByName("Piste cyclable");
    pisteCyclable2
        .setDefinition("Tronçons de chaussée en site propre, réservée aux cycles et cyclomoteurs. La longueur doit être supérieure à 200m. Les bandes cyclables sont exclues.");
    sProduit.createFeatureAttributeValue(nature2, "Sentier");
    FC_FeatureAttributeValue sentier2 = nature2
        .getFeatureAttributeValueByName("Sentier");
    sentier2
        .setDefinition("Chemins étroits ne permettant pas le passage de véhicules. Seuls les principaux sentiers sont inclus. Passerelles supportant une allée, directement reliés au réseau routier. Les passerelles ont une position par rapport au sol supérieure à 0.");
    sProduit.createFeatureAttributeValue(nature2, "Escalier");
    FC_FeatureAttributeValue escalier2 = nature2
        .getFeatureAttributeValueByName("Escalier");
    escalier2
        .setDefinition("Escaliers directement reliés au réseau routier supportant une allée, assurant la jonction entre deux voies de communication ou entre le réseau routier et un élément adressable. Sur rue, les escaliers visibles sur les photographies aériennes sont distingués quelle que soit leur longueur.");

    // Attribut NUMERO
    sProduit.createFeatureAttribute(routeNommee, "NUMERO", "string", false);
    AttributeType numero2 = routeNommee.getFeatureAttributeByName("NUMERO");
    numero2
        .setDefinition("Désigne le classement administratif d'un tronçon routier.");
    // sProduit.createFeatureAttributeValue(numero1, "NC");
    // FC_FeatureAttributeValue nc1 =
    // numero1.getFeatureAttributeValueByName("NC");
    // nc1.setDefinition("Non concerné : l’attribut « NUMERO » n’a pas lieu d’être renseigné (cas d’un chemin par exemple).");
    // sProduit.createFeatureAttributeValue(numero1, "NR");
    // FC_FeatureAttributeValue nr1 =
    // numero1.getFeatureAttributeValueByName("NR");
    // nc1.setDefinition("Non renseigné : même si le numéro n’est pas connu, le tronçon en porte peut-être un. L’information est manquante dans le produit.");

    // Attribut NOM_RUE_G
    sProduit.createFeatureAttribute(routeNommee, "NOM_RUE_G", "string", false);
    AttributeType nomRueG2 = routeNommee.getFeatureAttributeByName("NOM_RUE_G");
    nomRueG2
        .setDefinition("Nom rue gauche. Une rue est un ensemble de tronçons de route associés à un même nom. Une rue est identifiée par son nom dans une commune donnée. « NOM_RUE_G » est le nom porté par la rue pour la commune dont le numéro INSEE est identique à la valeur de l’attribut « INSEECOM_G ». Dans les résidences portant un nom avec des allées comportant également des noms, on garde le nom servant à l’adressage. Dans certains cas le toponyme qui sert à l’adressage est un nom de lieu-dit. Il servira à nommer la voie qui dessert ces adresses. Les lettres « LD » placées devant le toponyme serviront à les identifier.");

    // Attribut NOM_RUE_D
    sProduit.createFeatureAttribute(routeNommee, "NOM_RUE_D", "string", false);
    AttributeType nomRueD2 = routeNommee.getFeatureAttributeByName("NOM_RUE_D");
    nomRueD2
        .setDefinition("Nom rue droite. Une rue est un ensemble de tronçons de route associés à un même nom. Une rue est identifiée par son nom dans une commune donnée. « NOM_RUE_D » est le nom porté par la rue pour la commune dont le numéro INSEE est identique à la valeur de l’attribut « INSEECOM_D ». Dans les résidences portant un nom avec des allées comportant également des noms, on garde le nom servant à l’adressage. Dans certains cas le toponyme qui sert à l’adressage est un nom de lieu-dit. Il servira à nommer la voie qui dessert ces adresses. Les lettres « LD » placées devant le toponyme serviront à les identifier.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(routeNommee, "IMPORTANCE", "string", true);
    AttributeType importance2 = routeNommee
        .getFeatureAttributeByName("IMPORTANCE");
    importance2
        .setDefinition("Cet attribut matérialise une hiérarchisation du réseau routier fondée, non pas sur un critère administratif, mais sur l'importance des tronçons de route pour le trafic routier. Ainsi, les valeurs \"1\", \"2\", \"3\", \"4\", \"5\" permettent un maillage de plus en plus dense du territoire. Le graphe des éléments appartenant à un degré (autre que le plus bas) et aux niveaux supérieurs est connexe.");
    sProduit.createFeatureAttributeValue(importance2, "1");
    FC_FeatureAttributeValue un2 = importance2
        .getFeatureAttributeValueByName("1");
    un2.setDefinition("Le réseau 1 assure les liaisons entre métropoles et compose l’essentiel du réseau européen. Il est composé en général d’autoroutes et quasi-autoroutes, parfois de nationales.");
    sProduit.createFeatureAttributeValue(importance2, "2");
    FC_FeatureAttributeValue deux2 = importance2
        .getFeatureAttributeValueByName("2");
    deux2
        .setDefinition("Liaisons entre départements. Cette valeur représente une densification du maillage routier défini par les tronçons d’importance 1. Les liaisons d’importance 2 ont fonction d’assurer les liaisons à fort trafic à caractère prioritaire entre agglomérations importantes, d’assurer les liaisons des agglomérations importantes au réseau d’importance 1, d’offrir une alternative à une autoroute si celle-ci est payante, de proposer des itinéraires de contournement des agglomérations, d’assurer la continuité, en agglomération, des liaisons interurbaines à fort trafic quand il n’y a pas de contournement possible.");
    sProduit.createFeatureAttributeValue(importance2, "3");
    FC_FeatureAttributeValue trois2 = importance2
        .getFeatureAttributeValueByName("3");
    trois2
        .setDefinition("Liaisons ville à ville à l’intérieur d’un département. Ce niveau est majoritairement représenté par des routes départementales, toutefois certaines départementales peuvent avoir une importance 4 ou 5. Cette valeur représente une densification du maillage routier défini par les tronçons d’importance 2. Les liaisons d’importance 3 ont fonction de relier les communes de moindre importance entre elles (les chefs-lieux de canton en particulier), de desservir les localités et sites touristiques importants, de desservir les points de passage des obstacles naturels quand ils sont peu nombreux (cols routiers, ponts), de desservir les agglomérations d'où partent des liaisons maritimes, de structurer la circulation en agglomération.");
    sProduit.createFeatureAttributeValue(importance2, "4");
    FC_FeatureAttributeValue quatre2 = importance2
        .getFeatureAttributeValueByName("4");
    quatre2
        .setDefinition("Voies permettant de se déplacer rapidement à l’intérieur d’une commune et, dans les zones rurales, de relier le bourg aux hameaux proches. Cette valeur représente une densification du maillage routier défini par les tronçons d’importance 3. Les liaisons d’importance 4 ont fonction de structurer la circulation en agglomération, de relier le bourg aux hameaux proches.");
    sProduit.createFeatureAttributeValue(importance2, "5");
    FC_FeatureAttributeValue cinq2 = importance2
        .getFeatureAttributeValueByName("5");
    cinq2
        .setDefinition("Voies permettant de desservir l’intérieur d’une commune. Valeur prise par exclusion des autres valeurs de l'attribut.");
    sProduit.createFeatureAttributeValue(importance2, "NC");
    FC_FeatureAttributeValue nc2bis = importance2
        .getFeatureAttributeValueByName("NC");
    nc2bis.setDefinition("Non concerné par cet attribut.");
    sProduit.createFeatureAttributeValue(importance2, "NR");
    FC_FeatureAttributeValue nr2bis = importance2
        .getFeatureAttributeValueByName("NR");
    nr2bis.setDefinition("Non renseigné.");

    // Attribut CL_ADMIN
    sProduit.createFeatureAttribute(routeNommee, "CL_ADMIN", "string", true);
    AttributeType clAdmin2 = routeNommee.getFeatureAttributeByName("CL_ADMIN");
    clAdmin2
        .setDefinition("Classement administratif. Attribut précisant le statut d'une route numérotée ou nommée.");
    sProduit.createFeatureAttributeValue(clAdmin2, "Autoroute");
    FC_FeatureAttributeValue autoroute3 = clAdmin2
        .getFeatureAttributeValueByName("Autoroute");
    autoroute3
        .setDefinition("Les autoroutes sont des routes sans croisement, accessibles seulement en des points aménagés à cet effet et réservées aux véhicules à propulsion mécanique (Article L122-1 du code de la voirie routière).");
    sProduit.createFeatureAttributeValue(clAdmin2, "Nationale");
    FC_FeatureAttributeValue nationale2 = clAdmin2
        .getFeatureAttributeValueByName("Nationale");
    nationale2
        .setDefinition("Route nationale (voies du domaine public routier national autres que les autoroutes précédemment définies).");
    sProduit.createFeatureAttributeValue(clAdmin2, "Départementale");
    FC_FeatureAttributeValue departementale2 = clAdmin2
        .getFeatureAttributeValueByName("Départementale");
    departementale2
        .setDefinition("Voie qui fait partie du domaine public routier départemental.");
    sProduit.createFeatureAttributeValue(clAdmin2, "Autre");
    FC_FeatureAttributeValue autre3 = clAdmin2
        .getFeatureAttributeValueByName("Autre");
    autre3
        .setDefinition("Toute autre voie non classée dans les catégories administratives précédentes.");

    // Attribut GESTION
    sProduit.createFeatureAttribute(routeNommee, "GESTION", "string", false);
    AttributeType gestion2 = routeNommee.getFeatureAttributeByName("GESTION");
    gestion2
        .setDefinition("Gestionnaire. Définit le gestionnaire administratif d’une route. Toutes les routes classées possèdent un ‘Gestionnaire’. Il existe différentes catégories de routes pour lesquelles le gestionnaire diffère : pour les routes départementales, il s’agit du gestionnaire départemental de la route au sens administratif (c’est-à-dire le numéro de département),  pour les routes nationales et les autoroutes non concédées, le gestionnaire correspond également au gestionnaire départemental de la route (au sens administratif), pour les autoroutes concédées, le gestionnaire est la société concessionnaire d’autoroute (une correspondance est établie entre ces sociétés et un code en trois lettres), les routes codées sur les bretelles d’échangeurs identifiés prennent le gestionnaire de la route à laquelle l’échangeur est rattaché.");
    // Serait complétable au niveau des valeurs énumérées moyennant beaucoup
    // de
    // patience

    // Attribut MISE_SERV
    sProduit.createFeatureAttribute(routeNommee, "MISE_SERV", "string", false);
    AttributeType miseServ2 = routeNommee
        .getFeatureAttributeByName("MISE_SERV");
    miseServ2
        .setDefinition("Date de mise en service. Définit la date prévue ou la date effective de mise en service d’un tronçon de route. Cet attribut n'est rempli que pour les tronçons en construction, il est à “1000-01-01“ dans les autres cas. Les tronçons qui possèdent une date de mise en service sont complètement fermés aux véhicules avant cette date.");

    // Attribut IT_VERT
    sProduit.createFeatureAttribute(routeNommee, "IT_VERT", "string", false);
    AttributeType itVert2 = routeNommee.getFeatureAttributeByName("IT_VERT");
    itVert2
        .setDefinition("Itinéraire vert. Indique l’appartenance ou non d’un tronçon routier au réseau vert. Le réseau vert, composé de pôles verts et de liaisons vertes, couvre l’ensemble du territoire français.Les pôles verts sont composés de communes de plus de 23.000 habitants en province et de 39.000 habitants en Ile-de-France, ainsi que certains pôles d’activités administratifs, économiques, touristiques ou industriels. On retient seulement le réseau vert de transit entre pôles verts. Le réseau vert de rabattement, à l’intérieur des villes, et le réseau vert conseillé aux poids lourds ne sont pas retenus.");

    // Attribut IT_EUROP
    sProduit.createFeatureAttribute(routeNommee, "IT_EUROP", "string", false);
    AttributeType itEurop2 = routeNommee.getFeatureAttributeByName("IT_EUROP");
    itEurop2
        .setDefinition("Itinéraire européen. Numéro de route européenne : une route européenne emprunte en général le réseau autoroutier ou national (exceptionnellement départemental ou non classé).");

    // Attribut FICTIF
    sProduit.createFeatureAttribute(routeNommee, "FICTIF", "string", false);
    AttributeType fictif2 = routeNommee.getFeatureAttributeByName("FICTIF");
    fictif2
        .setDefinition("La valeur “oui“ indique que la géométrie du tronçon de route n'est pas significative. La présence de ce dernier sert à raccorder une bretelle à l’axe d’une chaussée afin d'assurer la continuité du réseau routier linéaire.");
    sProduit.createFeatureAttributeValue(fictif2, "Oui");
    sProduit.createFeatureAttributeValue(fictif2, "Non");

    // Attribut FRANCHISSMT
    sProduit.createFeatureAttribute(routeNommee, "FRANCHISSMT", "string", true);
    AttributeType franchissmt2 = routeNommee
        .getFeatureAttributeByName("FRANCHISSMT");
    franchissmt2
        .setDefinition("Franchissement.Cet attribut informe sur le niveau de l’objet par rapport à la surface du sol.");
    sProduit.createFeatureAttributeValue(franchissmt2, "Gué ou radier");
    FC_FeatureAttributeValue gue2 = franchissmt2
        .getFeatureAttributeValueByName("Gué ou radier");
    gue2.setDefinition("Passage naturel ou aménagé permettant aux véhicules de traverser un cours d’eau sans le recours d’un pont ou d’un bateau.");
    sProduit.createFeatureAttributeValue(franchissmt2, "Pont");
    FC_FeatureAttributeValue pont2 = franchissmt2
        .getFeatureAttributeValueByName("Pont");
    pont2
        .setDefinition("Tronçon de route situé au-dessus du niveau du sol (Ponceau, Pont, Pont mobile, Viaduc, Passerelle).");
    sProduit.createFeatureAttributeValue(franchissmt2, "Tunnel");
    FC_FeatureAttributeValue tunnel2 = franchissmt2
        .getFeatureAttributeValueByName("Tunnel");
    tunnel2
        .setDefinition("Tronçon de route situé sous le niveau du sol (Tunnel).");
    sProduit.createFeatureAttributeValue(franchissmt2, "NC");
    FC_FeatureAttributeValue nc4 = franchissmt2
        .getFeatureAttributeValueByName("NC");
    nc4.setDefinition("Tronçon de route situé au niveau du sol (y compris les tronçons en déblai et en remblai).");

    // Attribut Largeur
    sProduit.createFeatureAttribute(routeNommee, "LARGEUR", "float", false);
    AttributeType largeur2 = routeNommee.getFeatureAttributeByName("LARGEUR");
    largeur2
        .setDefinition("Largeur de chaussée. Largeur de chaussée (d’accotement à accotement) exprimée en mètres.");

    // Attribut NOM_ITI
    sProduit.createFeatureAttribute(routeNommee, "NOM_ITI", "string", false);
    AttributeType nomIti2 = routeNommee.getFeatureAttributeByName("NOM_ITI");
    nomIti2
        .setDefinition("Nom d’itinéraire. Définit un parcours routier nommé.");

    // Attribut NB_VOIES
    sProduit.createFeatureAttribute(routeNommee, "NB_VOIES", "integer", false);
    AttributeType nbVoies2 = routeNommee.getFeatureAttributeByName("NB_VOIES");
    nbVoies2
        .setDefinition("Nombre de voies.Nombre total de voies d’une route, d’une rue ou d’une chaussée de route à chaussées séparées.Lorsque les voies ne sont pas matérialisées, l’attribut indique le nombre maximum de voies de circulation effectivement utilisées dans des conditions normales de circulation.L'augmentation du nombre de voies au niveau d'un carrefour pour permettre de tourner plus facilement à droite ou à gauche n'est pas prise en compte, ainsi que les voies d'accélération ou de décélération des échangeurs d'autoroute.");

    // Attribut POS_SOL
    sProduit.createFeatureAttribute(routeNommee, "POS_SOL", "integer", false);
    AttributeType posSol2 = routeNommee.getFeatureAttributeByName("POS_SOL");
    posSol2
        .setDefinition("Position par rapport au sol. Donne le niveau de l’objet par rapport à la surface du sol (valeur négative pour un objet souterrain, nulle pour un objet au sol et positive pour un objet en sursol). Si l’objet en sursol passe au dessus d’autres objets en sursol, sa valeur « position par rapport au sol » est égale à « 1 + le nombre d’objets intercalés ». De la même façon, un souterrain peut prendre une valeur « position par rapport au sol » égale à « – 1 – le nombre d’objets souterrains intercalés ».");

    // Attribut SENS
    sProduit.createFeatureAttribute(routeNommee, "SENS", "string", true);
    AttributeType sens2 = routeNommee.getFeatureAttributeByName("SENS");
    sens2
        .setDefinition("Sens de circulation autorisée pour les automobiles sur les voies.");
    sProduit.createFeatureAttributeValue(sens2, "Double");
    FC_FeatureAttributeValue double2 = sens2
        .getFeatureAttributeValueByName("Double");
    double2.setDefinition("La circulation est autorisée dans les deux sens.");
    sProduit.createFeatureAttributeValue(sens2, "Direct");
    FC_FeatureAttributeValue direct2 = sens2
        .getFeatureAttributeValueByName("Direct");
    direct2
        .setDefinition("La circulation n’est autorisée que dans le sens de numérisation du tronçon.");
    sProduit.createFeatureAttributeValue(sens2, "Inverse");
    FC_FeatureAttributeValue inverse2 = sens2
        .getFeatureAttributeValueByName("Inverse");
    inverse2
        .setDefinition("La circulation n’est autorisée que dans le sens inverse de numérisation du tronçon.");
    sProduit.createFeatureAttributeValue(sens2, "NC");
    FC_FeatureAttributeValue nc5 = sens2.getFeatureAttributeValueByName("NC");
    nc5.setDefinition("Non concerné.");
    sProduit.createFeatureAttributeValue(sens2, "NR");
    FC_FeatureAttributeValue nr5 = sens2.getFeatureAttributeValueByName("NR");
    nr5.setDefinition("Non renseigné : l’information est manquante dans la base.");

    // Attribut INSEECOM_G
    sProduit.createFeatureAttribute(routeNommee, "INSEECOM_G", "string", false);
    AttributeType inseecom_g2 = routeNommee
        .getFeatureAttributeByName("INSEECOM_G");
    inseecom_g2
        .setDefinition("INSEE Commune gauche. Numéro d’INSEE de la commune à gauche du tronçon par rapport à son sens de numérisation.");

    // Attribut INSEECOM_D
    sProduit.createFeatureAttribute(routeNommee, "INSEECOM_D", "string", false);
    AttributeType inseecom_d2 = routeNommee
        .getFeatureAttributeByName("INSEECOM_D");
    inseecom_d2
        .setDefinition("INSEE Commune droite. Numéro d’INSEE de la commune à droite du tronçon par rapport à son sens de numérisation.");

    // Attribut CODEVOIE_G
    sProduit.createFeatureAttribute(routeNommee, "CODEVOIE_G", "string", false);
    AttributeType codevoie_g2 = routeNommee
        .getFeatureAttributeByName("CODEVOIE_G");
    codevoie_g2
        .setDefinition("Identifiant gauche. Identifiant de la voie associée au côté gauche du tronçon.Il est obtenu par concaténation du numéro d’INSEE (5 caractères) et du code Rivoli (4 caractères) de la voie associée au côté gauche du tronçon.");

    // Attribut CODEVOIE_D
    sProduit.createFeatureAttribute(routeNommee, "CODEVOIE_D", "string", false);
    AttributeType codevoie_d2 = routeNommee
        .getFeatureAttributeByName("CODEVOIE_D");
    codevoie_d2
        .setDefinition("Identifiant droite. Identifiant de la voie associée au côté droit du tronçon.Il est obtenu par concaténation du numéro d’INSEE (5 caractères) et du code Rivoli (4 caractères) de la voie associée au côté droit du tronçon.");

    // Attribut TYP_ADRES
    sProduit.createFeatureAttribute(routeNommee, "TYP_ADRES", "string", true);
    AttributeType typAdres2 = routeNommee
        .getFeatureAttributeByName("TYP_ADRES");
    typAdres2
        .setDefinition("Type d’adressage. Renseigne sur le type d’adressage du tronçon.");
    sProduit.createFeatureAttributeValue(typAdres2, "Classique");
    FC_FeatureAttributeValue clasic2 = typAdres2
        .getFeatureAttributeValueByName("Classique");
    clasic2
        .setDefinition("Un côté de la rue porte des numéros pairs, l’autre des numéros impairs. Les numéros sont ordonnés par ordre croissant ou décroissant le long de la rue.");
    sProduit.createFeatureAttributeValue(typAdres2, "Métrique");
    FC_FeatureAttributeValue metric2 = typAdres2
        .getFeatureAttributeValueByName("Métrique");
    metric2
        .setDefinition("Les numéros des bornes postales correspondent à la distance en mètres qui sépare l’entrée principale de la parcelle d’un point origine arbitraire de la rue. Le principe de côté pair et impair n’est pas toujours conservé.");
    sProduit.createFeatureAttributeValue(typAdres2, "Linéaire");
    FC_FeatureAttributeValue lineaire2 = typAdres2
        .getFeatureAttributeValueByName("Linéaire");
    lineaire2
        .setDefinition("Les numéros sont ordonnés le long de chaque côté de la rue, mais sans distinction pair ou impair.");
    sProduit.createFeatureAttributeValue(typAdres2, "Autre");
    FC_FeatureAttributeValue autre4 = typAdres2
        .getFeatureAttributeValueByName("Autre");
    autre4
        .setDefinition("Ni classique, ni métrique, ni linéaire. Les numéros ne sont pas ordonnés.");
    sProduit.createFeatureAttributeValue(typAdres2, "NC");
    FC_FeatureAttributeValue nc6 = typAdres2
        .getFeatureAttributeValueByName("NC");
    nc6.setDefinition("Non concerné.");
    sProduit.createFeatureAttributeValue(typAdres2, "NR");
    FC_FeatureAttributeValue nr6 = typAdres2
        .getFeatureAttributeValueByName("NR");
    nr6.setDefinition("Non renseigné.");

    // Attribut BORNEDEB_G
    sProduit
        .createFeatureAttribute(routeNommee, "BORNEDEB_G", "integer", false);
    AttributeType bornedeb_g2 = routeNommee
        .getFeatureAttributeByName("BORNEDEB_G");
    bornedeb_g2
        .setDefinition("Borne début gauche. Numéro de borne à gauche du tronçon en son sommet initial.");

    // Attribut BORNEDEB_D
    sProduit
        .createFeatureAttribute(routeNommee, "BORNEDEB_D", "integer", false);
    AttributeType bornedeb_d2 = routeNommee
        .getFeatureAttributeByName("BORNEDEB_D");
    bornedeb_d2
        .setDefinition("Borne début droite. Numéro de borne à droite du tronçon en son sommet initial.");

    // Attribut BORNEFIN_G
    sProduit
        .createFeatureAttribute(routeNommee, "BORNEFIN_G", "integer", false);
    AttributeType bornefin_g2 = routeNommee
        .getFeatureAttributeByName("BORNEFIN_G");
    bornefin_g2
        .setDefinition("Borne fin gauche. Numéro de borne à gauche du tronçon en son sommet final.");

    // Attribut BORNEFIN_D
    sProduit
        .createFeatureAttribute(routeNommee, "BORNEFIN_D", "integer", false);
    AttributeType bornefin_d2 = routeNommee
        .getFeatureAttributeByName("BORNEFIN_D");
    bornefin_d2
        .setDefinition("Borne fin droite. Numéro de borne à droite du tronçon en son sommet final.");

    // Attribut ETAT
    sProduit.createFeatureAttribute(routeNommee, "ETAT", "string", false);
    AttributeType etat2 = routeNommee.getFeatureAttributeByName("ETAT");
    etat2.setDefinition("Etat du tronçon.");

    // Attribut Z_INI
    sProduit.createFeatureAttribute(routeNommee, "Z_INI", "float", false);
    AttributeType zini2 = routeNommee.getFeatureAttributeByName("Z_INI");
    zini2
        .setDefinition("Altitude initiale : c’est l’altitude du sommet initial du tronçon.");

    // Attribut Z_FIN
    sProduit.createFeatureAttribute(routeNommee, "Z_FIN", "float", false);
    AttributeType zfin2 = routeNommee.getFeatureAttributeByName("Z_FIN");
    zfin2
        .setDefinition("Altitude finale : c’est l’altitude du sommet final du tronçon.");

    // Classe Chemin///////////////////////////////////////////////////

    sProduit.createFeatureType("CHEMIN");
    FeatureType chemin5 = (FeatureType) (sProduit
        .getFeatureTypeByName("CHEMIN"));
    chemin5
        .setDefinition("Voie de communication terrestre non ferrée destinée aux piétons, aux cycles ou aux animaux, ou route sommairement revêtue (pas de revêtement de surface ou revêtement de surface fortement dégradé).");
    chemin5.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(chemin5, "ID", "string", false);
    AttributeType id6 = chemin5.getFeatureAttributeByName("ID");
    id6.setDefinition("Identifiant du tronçon de chemin. identifiant est unique. Il est stable d’une édition à l’autre. ");

    // Attribut PREC_PLANI
    sProduit.createFeatureAttribute(chemin5, "PREC_PLANI", "float", true);
    AttributeType prec_plani5 = chemin5.getFeatureAttributeByName("PREC_PLANI");
    prec_plani5
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani5, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani5, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani5, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani5, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani5, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani5, "30.0");

    // Attribut PREC_ALTI
    sProduit.createFeatureAttribute(chemin5, "PREC_ALTI", "float", true);
    AttributeType prec_alti5 = chemin5.getFeatureAttributeByName("PREC_ALTI");
    prec_alti5
        .setDefinition("Précision géométrique altimétrique. Attribut précisant la précision géométrique en altimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_alti5, "1.0");
    sProduit.createFeatureAttributeValue(prec_alti5, "2.5");
    sProduit.createFeatureAttributeValue(prec_alti5, "20.0");
    sProduit.createFeatureAttributeValue(prec_alti5, "9999.0");

    // Attribut NATURE
    sProduit.createFeatureAttribute(chemin5, "NATURE", "string", true);
    AttributeType nature5 = chemin5.getFeatureAttributeByName("NATURE");
    nature5
        .setDefinition("Attribut permettant de distinguer différentes natures de tronçon de chemin.");
    sProduit.createFeatureAttributeValue(nature5, "Chemin");
    FC_FeatureAttributeValue chemin6 = nature5
        .getFeatureAttributeValueByName("Chemin");
    chemin6
        .setDefinition("Les chemins sont prévus pour la circulation de véhicules ou d’engins d’exploitation. Ils ne sont pas forcément carrossables pour tous et par tout temps (voir aussi « route empierrée »).");
    sProduit.createFeatureAttributeValue(nature5, "Escalier");
    FC_FeatureAttributeValue escalier5 = nature5
        .getFeatureAttributeValueByName("Escalier");
    escalier5
        .setDefinition("Escalier directement relié au réseau routier supportant une allée, assurant la jonction entre deux voies de communication ou entre le réseau routier et un élément adressable. Sur rue, les escaliers visibles sur les photographies aériennes sont distingués quelle que soit leur longueur.");
    sProduit.createFeatureAttributeValue(nature5, "Sentier");
    FC_FeatureAttributeValue sentier5 = nature5
        .getFeatureAttributeValueByName("Sentier");
    sentier5
        .setDefinition("Chemin étroit ne permettant pas le passage de véhicules. Seuls les principaux sentiers sont inclus. Passerelle supportant une allée, directement reliés au réseau routier. Les passerelles ont une position par rapport au sol supérieure à 0.");
    sProduit.createFeatureAttributeValue(nature5, "Piste Cyclable");
    FC_FeatureAttributeValue pisteCyclable5 = nature5
        .getFeatureAttributeValueByName("Piste Cyclable");
    pisteCyclable5
        .setDefinition("Tronçon de chaussée en site propre, réservée aux cycles et cyclomoteurs. La longueur doit être supérieure à 200m. Les bandes cyclables sont exclues.");

    // Attribut FRANCHISSMT
    sProduit.createFeatureAttribute(chemin5, "FRANCHISSMT", "string", true);
    AttributeType franchissmt5 = chemin5
        .getFeatureAttributeByName("FRANCHISSMT");
    franchissmt5
        .setDefinition("Franchissement.Cet attribut informe sur le niveau de l’objet par rapport à la surface du sol.");
    sProduit.createFeatureAttributeValue(franchissmt5, "Gué ou radier");
    FC_FeatureAttributeValue gue5 = franchissmt5
        .getFeatureAttributeValueByName("Gué ou radier");
    gue5.setDefinition("Passage naturel ou aménagé permettant aux véhicules de traverser un cours d’eau sans le recours d’un pont ou d’un bateau.");
    sProduit.createFeatureAttributeValue(franchissmt5, "Pont");
    FC_FeatureAttributeValue pont5 = franchissmt5
        .getFeatureAttributeValueByName("Pont");
    pont5
        .setDefinition("Tronçon de route situé au-dessus du niveau du sol (Ponceau, Pont, Pont mobile, Viaduc, Passerelle).");
    sProduit.createFeatureAttributeValue(franchissmt5, "Tunnel");
    FC_FeatureAttributeValue tunnel5 = franchissmt5
        .getFeatureAttributeValueByName("Tunnel");
    tunnel5
        .setDefinition("Tronçon de route situé sous le niveau du sol (Tunnel).");
    sProduit.createFeatureAttributeValue(franchissmt5, "NC");
    FC_FeatureAttributeValue nc15 = franchissmt5
        .getFeatureAttributeValueByName("NC");
    nc15.setDefinition("Tronçon de route situé au niveau du sol (y compris les tronçons en déblai et en remblai).");

    // Attribut NOM_ITI
    sProduit.createFeatureAttribute(chemin5, "NOM_ITI", "string", false);
    AttributeType nomIti5 = chemin5.getFeatureAttributeByName("NOM_ITI");
    nomIti5
        .setDefinition("Nom d’itinéraire. Définit un parcours routier nommé.");

    // Attribut POS_SOL
    sProduit.createFeatureAttribute(chemin5, "POS_SOL", "integer", false);
    AttributeType posSol5 = chemin5.getFeatureAttributeByName("POS_SOL");
    posSol5
        .setDefinition("Position par rapport au sol. Donne le niveau de l’objet par rapport à la surface du sol (valeur négative pour un objet souterrain, nulle pour un objet au sol et positive pour un objet en sursol). Si l’objet en sursol passe au dessus d’autres objets en sursol, sa valeur « position par rapport au sol » est égale à « 1 + le nombre d’objets intercalés ». De la même façon, un souterrain peut prendre une valeur « position par rapport au sol » égale à « – 1 – le nombre d’objets souterrains intercalés ».");

    // Attribut Z_INI
    sProduit.createFeatureAttribute(chemin5, "Z_INI", "float", false);
    AttributeType zini5 = chemin5.getFeatureAttributeByName("Z_INI");
    zini5
        .setDefinition("Altitude initiale : c’est l’altitude du sommet initial du tronçon.");

    // Attribut Z_FIN
    sProduit.createFeatureAttribute(chemin5, "Z_FIN", "float", false);
    AttributeType zfin5 = chemin5.getFeatureAttributeByName("Z_FIN");
    zfin5
        .setDefinition("Altitude finale : c’est l’altitude du sommet final du tronçon.");

    // Classe Route
    // primaire///////////////////////////////////////////////////

    sProduit.createFeatureType("ROUTE_PRIMAIRE");
    FeatureType routePrimaire = (FeatureType) (sProduit
        .getFeatureTypeByName("ROUTE_PRIMAIRE"));
    routePrimaire
        .setDefinition("Portion de voie de communication destinée aux automobiles, aux piétons, aux cycles ou aux animaux, homogène pour l'ensemble des attributs et des relations qui la concernent. Cette classe est un sous-ensemble de la classe ROUTE, et comprend uniquement les tronçons de route d’importance 1 ou 2. Cela permet de n’utiliser ou de n’afficher que le réseau dit principal, pour des raisons de faciliter de manipulation ou de lisibilité à l’écran suivant l’échelle.");
    routePrimaire.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(routePrimaire, "ID", "string", false);
    AttributeType id3 = routePrimaire.getFeatureAttributeByName("ID");
    id3.setDefinition("Identifiant Tronçon.Cet identifiant est unique. Il est stable d’une édition à l’autre. Il permet aussi d’établir un lien entre le ponctuel de la classe « ADRESSE » des produits BD ADRESSE® et POINT ADRESSE® (par l’intermédiaire de l’attribut ID_TR) et l’objet linéaire de la classe « ROUTE ».");

    // Attribut PREC_PLANI
    sProduit.createFeatureAttribute(routePrimaire, "PREC_PLANI", "float", true);
    AttributeType prec_plani3 = routePrimaire
        .getFeatureAttributeByName("PREC_PLANI");
    prec_plani3
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani3, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani3, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani3, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani3, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani3, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani3, "30.0");

    // Attribut PREC_ALTI
    sProduit.createFeatureAttribute(routePrimaire, "PREC_ALTI", "float", true);
    AttributeType prec_alti3 = routePrimaire
        .getFeatureAttributeByName("PREC_ALTI");
    prec_alti3
        .setDefinition("Précision géométrique altimétrique. Attribut précisant la précision géométrique en altimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_alti3, "1.0");
    sProduit.createFeatureAttributeValue(prec_alti3, "2.5");
    sProduit.createFeatureAttributeValue(prec_alti3, "20.0");
    sProduit.createFeatureAttributeValue(prec_alti3, "9999.0");

    // Attribut NATURE
    sProduit.createFeatureAttribute(routePrimaire, "NATURE", "string", true);
    AttributeType nature3 = routePrimaire.getFeatureAttributeByName("NATURE");
    nature3
        .setDefinition("Attribut permettant de distinguer différentes natures de tronçon de route.");
    sProduit.createFeatureAttributeValue(nature3, "Autoroute");
    FC_FeatureAttributeValue autoroute4 = nature3
        .getFeatureAttributeValueByName("Autoroute");
    autoroute4
        .setDefinition("Routes sans croisement, accessibles seulement en des points aménagés à cet effet et réservées aux véhicules à propulsion mécanique. Le classement dans la catégorie des autoroutes est prononcé par décret du conseil d’état, pris après enquête publique.");
    sProduit.createFeatureAttributeValue(nature3, "Quasi-autoroute");
    FC_FeatureAttributeValue quasiautoroute3 = nature3
        .getFeatureAttributeValueByName("Quasi-autoroute");
    quasiautoroute3
        .setDefinition("Routes de même définition que l’autoroute mais non classées officiellement dans cette catégorie. Ce sont des routes à chaussées séparées par un terre-plein central, qui ne possèdent pas de croisement à niveau avec le reste du réseau routier.");
    sProduit.createFeatureAttributeValue(nature3, "Bretelle");
    FC_FeatureAttributeValue bretelle3 = nature3
        .getFeatureAttributeValueByName("Bretelle");
    bretelle3
        .setDefinition("Bretelles de liaison (ou d’échangeur) ou voies d’accès à une Aire de Service ou de Repos.");
    sProduit.createFeatureAttributeValue(nature3, "Route à 2 chaussées");
    FC_FeatureAttributeValue routeA2Chaussees3 = nature3
        .getFeatureAttributeValueByName("Route à 2 chaussées");
    routeA2Chaussees3
        .setDefinition("Routes comportant 2 chaussées séparées par un obstacle physique éventuellement ouvert aux carrefours. Elles possèdent donc des croisements à niveau, ce qui leur interdit d'être classées dans la catégorie Autoroute ou Quasi-autoroute.");
    sProduit.createFeatureAttributeValue(nature3, "Route à 1 chaussée");
    FC_FeatureAttributeValue routeA1Chaussee3 = nature3
        .getFeatureAttributeValueByName("Route à 1 chaussée");
    routeA1Chaussee3
        .setDefinition("Routes comportant 1 chaussée.Toutes les routes goudronnées qui ne sont pas classées en Route à 2 chaussées, Quasi-autoroute ou Autoroute se retrouvent dans cette classe.");
    sProduit.createFeatureAttributeValue(nature3, "Route empierrée");
    FC_FeatureAttributeValue routeEmpierree3 = nature3
        .getFeatureAttributeValueByName("Route empierrée");
    routeEmpierree3
        .setDefinition("Routes sommairement revêtues (pas de revêtement de surface ou revêtement très dégradé), mais permettant la circulation de véhicules automobiles de tourisme par tout temps. Toutes les routes empierrées sont incluses.");
    sProduit.createFeatureAttributeValue(nature3, "Chemin");
    FC_FeatureAttributeValue chemin3 = nature3
        .getFeatureAttributeValueByName("Chemin");
    chemin3
        .setDefinition("Les chemins sont prévus pour la circulation de véhicules ou d’engins d’exploitation. Ils ne sont pas forcément carrossables pour tous les véhicules et par tout temps (voir aussi “route empierrée”).");
    sProduit.createFeatureAttributeValue(nature3, "Bac auto");
    FC_FeatureAttributeValue bacAuto3 = nature3
        .getFeatureAttributeValueByName("Bac auto");
    bacAuto3
        .setDefinition("Trajets du bateau servant à passer des véhicules d’une rive à l’autre d’un cours d’eau ou d’un bras de mer.");
    sProduit.createFeatureAttributeValue(nature3, "Bac piéton");
    FC_FeatureAttributeValue bacPieton3 = nature3
        .getFeatureAttributeValueByName("Bac piéton");
    bacPieton3
        .setDefinition("Trajets du bateau servant à passer des piétons d’une rive à l’autre d’un cours d’eau ou d’un bras de mer.");
    sProduit.createFeatureAttributeValue(nature3, "Piste cyclable");
    FC_FeatureAttributeValue pisteCyclable3 = nature3
        .getFeatureAttributeValueByName("Piste cyclable");
    pisteCyclable3
        .setDefinition("Tronçons de chaussée en site propre, réservée aux cycles et cyclomoteurs. La longueur doit être supérieure à 200m. Les bandes cyclables sont exclues.");
    sProduit.createFeatureAttributeValue(nature3, "Sentier");
    FC_FeatureAttributeValue sentier3 = nature3
        .getFeatureAttributeValueByName("Sentier");
    sentier3
        .setDefinition("Chemins étroits ne permettant pas le passage de véhicules. Seuls les principaux sentiers sont inclus. Passerelles supportant une allée, directement reliés au réseau routier. Les passerelles ont une position par rapport au sol supérieure à 0.");
    sProduit.createFeatureAttributeValue(nature3, "Escalier");
    FC_FeatureAttributeValue escalier3 = nature3
        .getFeatureAttributeValueByName("Escalier");
    escalier3
        .setDefinition("Escaliers directement reliés au réseau routier supportant une allée, assurant la jonction entre deux voies de communication ou entre le réseau routier et un élément adressable. Sur rue, les escaliers visibles sur les photographies aériennes sont distingués quelle que soit leur longueur.");

    // Attribut NUMERO
    sProduit.createFeatureAttribute(routePrimaire, "NUMERO", "string", false);
    AttributeType numero3 = routePrimaire.getFeatureAttributeByName("NUMERO");
    numero3
        .setDefinition("Désigne le classement administratif d'un tronçon routier.");

    // Attribut NOM_RUE_G
    sProduit
        .createFeatureAttribute(routePrimaire, "NOM_RUE_G", "string", false);
    AttributeType nomRueG3 = routePrimaire
        .getFeatureAttributeByName("NOM_RUE_G");
    nomRueG3
        .setDefinition("Nom rue gauche. Une rue est un ensemble de tronçons de route associés à un même nom. Une rue est identifiée par son nom dans une commune donnée. « NOM_RUE_G » est le nom porté par la rue pour la commune dont le numéro INSEE est identique à la valeur de l’attribut « INSEECOM_G ». Dans les résidences portant un nom avec des allées comportant également des noms, on garde le nom servant à l’adressage. Dans certains cas le toponyme qui sert à l’adressage est un nom de lieu-dit. Il servira à nommer la voie qui dessert ces adresses. Les lettres « LD » placées devant le toponyme serviront à les identifier.");

    // Attribut NOM_RUE_D
    sProduit
        .createFeatureAttribute(routePrimaire, "NOM_RUE_D", "string", false);
    AttributeType nomRueD3 = routePrimaire
        .getFeatureAttributeByName("NOM_RUE_D");
    nomRueD3
        .setDefinition("Nom rue droite. Une rue est un ensemble de tronçons de route associés à un même nom. Une rue est identifiée par son nom dans une commune donnée. « NOM_RUE_D » est le nom porté par la rue pour la commune dont le numéro INSEE est identique à la valeur de l’attribut « INSEECOM_D ». Dans les résidences portant un nom avec des allées comportant également des noms, on garde le nom servant à l’adressage. Dans certains cas le toponyme qui sert à l’adressage est un nom de lieu-dit. Il servira à nommer la voie qui dessert ces adresses. Les lettres « LD » placées devant le toponyme serviront à les identifier.");

    // Attribut IMPORTANCE
    sProduit
        .createFeatureAttribute(routePrimaire, "IMPORTANCE", "string", true);
    AttributeType importance3 = routePrimaire
        .getFeatureAttributeByName("IMPORTANCE");
    importance3
        .setDefinition("Cet attribut matérialise une hiérarchisation du réseau routier fondée, non pas sur un critère administratif, mais sur l'importance des tronçons de route pour le trafic routier. Ainsi, les valeurs \"1\", \"2\", \"3\", \"4\", \"5\" permettent un maillage de plus en plus dense du territoire. Le graphe des éléments appartenant à un degré (autre que le plus bas) et aux niveaux supérieurs est connexe.");
    sProduit.createFeatureAttributeValue(importance3, "1");
    FC_FeatureAttributeValue un3 = importance3
        .getFeatureAttributeValueByName("1");
    un3.setDefinition("Le réseau 1 assure les liaisons entre métropoles et compose l’essentiel du réseau européen. Il est composé en général d’autoroutes et quasi-autoroutes, parfois de nationales.");
    sProduit.createFeatureAttributeValue(importance3, "2");
    FC_FeatureAttributeValue deux3 = importance3
        .getFeatureAttributeValueByName("2");
    deux3
        .setDefinition("Liaisons entre départements. Cette valeur représente une densification du maillage routier défini par les tronçons d’importance 1. Les liaisons d’importance 2 ont fonction d’assurer les liaisons à fort trafic à caractère prioritaire entre agglomérations importantes, d’assurer les liaisons des agglomérations importantes au réseau d’importance 1, d’offrir une alternative à une autoroute si celle-ci est payante, de proposer des itinéraires de contournement des agglomérations, d’assurer la continuité, en agglomération, des liaisons interurbaines à fort trafic quand il n’y a pas de contournement possible.");
    sProduit.createFeatureAttributeValue(importance3, "3");
    FC_FeatureAttributeValue trois3 = importance3
        .getFeatureAttributeValueByName("3");
    trois3
        .setDefinition("Liaisons ville à ville à l’intérieur d’un département. Ce niveau est majoritairement représenté par des routes départementales, toutefois certaines départementales peuvent avoir une importance 4 ou 5. Cette valeur représente une densification du maillage routier défini par les tronçons d’importance 2. Les liaisons d’importance 3 ont fonction de relier les communes de moindre importance entre elles (les chefs-lieux de canton en particulier), de desservir les localités et sites touristiques importants, de desservir les points de passage des obstacles naturels quand ils sont peu nombreux (cols routiers, ponts), de desservir les agglomérations d'où partent des liaisons maritimes, de structurer la circulation en agglomération.");
    sProduit.createFeatureAttributeValue(importance3, "4");
    FC_FeatureAttributeValue quatre3 = importance3
        .getFeatureAttributeValueByName("4");
    quatre3
        .setDefinition("Voies permettant de se déplacer rapidement à l’intérieur d’une commune et, dans les zones rurales, de relier le bourg aux hameaux proches. Cette valeur représente une densification du maillage routier défini par les tronçons d’importance 3. Les liaisons d’importance 4 ont fonction de structurer la circulation en agglomération, de relier le bourg aux hameaux proches.");
    sProduit.createFeatureAttributeValue(importance3, "5");
    FC_FeatureAttributeValue cinq3 = importance3
        .getFeatureAttributeValueByName("5");
    cinq3
        .setDefinition("Voies permettant de desservir l’intérieur d’une commune. Valeur prise par exclusion des autres valeurs de l'attribut.");
    sProduit.createFeatureAttributeValue(importance3, "NC");
    FC_FeatureAttributeValue nc3bis = importance3
        .getFeatureAttributeValueByName("NC");
    nc3bis.setDefinition("Non concerné par cet attribut.");
    sProduit.createFeatureAttributeValue(importance3, "NR");
    FC_FeatureAttributeValue nr3bis = importance3
        .getFeatureAttributeValueByName("NR");
    nr3bis.setDefinition("Non renseigné.");

    // Attribut CL_ADMIN
    sProduit.createFeatureAttribute(routePrimaire, "CL_ADMIN", "string", true);
    AttributeType clAdmin3 = routePrimaire
        .getFeatureAttributeByName("CL_ADMIN");
    clAdmin3
        .setDefinition("Classement administratif. Attribut précisant le statut d'une route numérotée ou nommée.");
    sProduit.createFeatureAttributeValue(clAdmin3, "Autoroute");
    FC_FeatureAttributeValue autoroute5 = clAdmin3
        .getFeatureAttributeValueByName("Autoroute");
    autoroute5
        .setDefinition("Les autoroutes sont des routes sans croisement, accessibles seulement en des points aménagés à cet effet et réservées aux véhicules à propulsion mécanique (Article L122-1 du code de la voirie routière).");
    sProduit.createFeatureAttributeValue(clAdmin3, "Nationale");
    FC_FeatureAttributeValue nationale3 = clAdmin3
        .getFeatureAttributeValueByName("Nationale");
    nationale3
        .setDefinition("Route nationale (voies du domaine public routier national autres que les autoroutes précédemment définies).");
    sProduit.createFeatureAttributeValue(clAdmin3, "Départementale");
    FC_FeatureAttributeValue departementale3 = clAdmin3
        .getFeatureAttributeValueByName("Départementale");
    departementale3
        .setDefinition("Voie qui fait partie du domaine public routier départemental.");
    sProduit.createFeatureAttributeValue(clAdmin3, "Autre");
    FC_FeatureAttributeValue autre5 = clAdmin3
        .getFeatureAttributeValueByName("Autre");
    autre5
        .setDefinition("Toute autre voie non classée dans les catégories administratives précédentes.");

    // Attribut GESTION
    sProduit.createFeatureAttribute(routePrimaire, "GESTION", "string", false);
    AttributeType gestion3 = routePrimaire.getFeatureAttributeByName("GESTION");
    gestion3
        .setDefinition("Gestionnaire. Définit le gestionnaire administratif d’une route. Toutes les routes classées possèdent un ‘Gestionnaire’. Il existe différentes catégories de routes pour lesquelles le gestionnaire diffère : pour les routes départementales, il s’agit du gestionnaire départemental de la route au sens administratif (c’est-à-dire le numéro de département),  pour les routes nationales et les autoroutes non concédées, le gestionnaire correspond également au gestionnaire départemental de la route (au sens administratif), pour les autoroutes concédées, le gestionnaire est la société concessionnaire d’autoroute (une correspondance est établie entre ces sociétés et un code en trois lettres), les routes codées sur les bretelles d’échangeurs identifiés prennent le gestionnaire de la route à laquelle l’échangeur est rattaché.");
    // Serait complétable au niveau des valeurs énumérées moyennant beaucoup
    // de
    // patience

    // Attribut MISE_SERV
    sProduit
        .createFeatureAttribute(routePrimaire, "MISE_SERV", "string", false);
    AttributeType miseServ3 = routePrimaire
        .getFeatureAttributeByName("MISE_SERV");
    miseServ3
        .setDefinition("Date de mise en service. Définit la date prévue ou la date effective de mise en service d’un tronçon de route. Cet attribut n'est rempli que pour les tronçons en construction, il est à “1000-01-01“ dans les autres cas. Les tronçons qui possèdent une date de mise en service sont complètement fermés aux véhicules avant cette date.");

    // Attribut IT_VERT
    sProduit.createFeatureAttribute(routePrimaire, "IT_VERT", "string", false);
    AttributeType itVert3 = routePrimaire.getFeatureAttributeByName("IT_VERT");
    itVert3
        .setDefinition("Itinéraire vert. Indique l’appartenance ou non d’un tronçon routier au réseau vert. Le réseau vert, composé de pôles verts et de liaisons vertes, couvre l’ensemble du territoire français.Les pôles verts sont composés de communes de plus de 23.000 habitants en province et de 39.000 habitants en Ile-de-France, ainsi que certains pôles d’activités administratifs, économiques, touristiques ou industriels. On retient seulement le réseau vert de transit entre pôles verts. Le réseau vert de rabattement, à l’intérieur des villes, et le réseau vert conseillé aux poids lourds ne sont pas retenus.");

    // Attribut IT_EUROP
    sProduit.createFeatureAttribute(routePrimaire, "IT_EUROP", "string", false);
    AttributeType itEurop3 = routePrimaire
        .getFeatureAttributeByName("IT_EUROP");
    itEurop3
        .setDefinition("Itinéraire européen. Numéro de route européenne : une route européenne emprunte en général le réseau autoroutier ou national (exceptionnellement départemental ou non classé).");

    // Attribut FICTIF
    sProduit.createFeatureAttribute(routePrimaire, "FICTIF", "string", false);
    AttributeType fictif3 = routePrimaire.getFeatureAttributeByName("FICTIF");
    fictif3
        .setDefinition("La valeur “oui“ indique que la géométrie du tronçon de route n'est pas significative. La présence de ce dernier sert à raccorder une bretelle à l’axe d’une chaussée afin d'assurer la continuité du réseau routier linéaire.");
    sProduit.createFeatureAttributeValue(fictif3, "Oui");
    sProduit.createFeatureAttributeValue(fictif3, "Non");

    // Attribut FRANCHISSMT
    sProduit.createFeatureAttribute(routePrimaire, "FRANCHISSMT", "string",
        true);
    AttributeType franchissmt3 = routePrimaire
        .getFeatureAttributeByName("FRANCHISSMT");
    franchissmt3
        .setDefinition("Franchissement.Cet attribut informe sur le niveau de l’objet par rapport à la surface du sol.");
    sProduit.createFeatureAttributeValue(franchissmt3, "Gué ou radier");
    FC_FeatureAttributeValue gue3 = franchissmt3
        .getFeatureAttributeValueByName("Gué ou radier");
    gue3.setDefinition("Passage naturel ou aménagé permettant aux véhicules de traverser un cours d’eau sans le recours d’un pont ou d’un bateau.");
    sProduit.createFeatureAttributeValue(franchissmt3, "Pont");
    FC_FeatureAttributeValue pont3 = franchissmt3
        .getFeatureAttributeValueByName("Pont");
    pont3
        .setDefinition("Tronçon de route situé au-dessus du niveau du sol (Ponceau, Pont, Pont mobile, Viaduc, Passerelle).");
    sProduit.createFeatureAttributeValue(franchissmt3, "Tunnel");
    FC_FeatureAttributeValue tunnel3 = franchissmt3
        .getFeatureAttributeValueByName("Tunnel");
    tunnel3
        .setDefinition("Tronçon de route situé sous le niveau du sol (Tunnel).");
    sProduit.createFeatureAttributeValue(franchissmt3, "NC");
    FC_FeatureAttributeValue nc7 = franchissmt3
        .getFeatureAttributeValueByName("NC");
    nc7.setDefinition("Tronçon de route situé au niveau du sol (y compris les tronçons en déblai et en remblai).");

    // Attribut Largeur
    sProduit.createFeatureAttribute(routePrimaire, "LARGEUR", "float", false);
    AttributeType largeur3 = routePrimaire.getFeatureAttributeByName("LARGEUR");
    largeur3
        .setDefinition("Largeur de chaussée. Largeur de chaussée (d’accotement à accotement) exprimée en mètres.");

    // Attribut NOM_ITI
    sProduit.createFeatureAttribute(routePrimaire, "NOM_ITI", "string", false);
    AttributeType nomIti3 = routePrimaire.getFeatureAttributeByName("NOM_ITI");
    nomIti3
        .setDefinition("Nom d’itinéraire. Définit un parcours routier nommé.");

    // Attribut NB_VOIES
    sProduit
        .createFeatureAttribute(routePrimaire, "NB_VOIES", "integer", false);
    AttributeType nbVoies3 = routePrimaire
        .getFeatureAttributeByName("NB_VOIES");
    nbVoies3
        .setDefinition("Nombre de voies.Nombre total de voies d’une route, d’une rue ou d’une chaussée de route à chaussées séparées.Lorsque les voies ne sont pas matérialisées, l’attribut indique le nombre maximum de voies de circulation effectivement utilisées dans des conditions normales de circulation.L'augmentation du nombre de voies au niveau d'un carrefour pour permettre de tourner plus facilement à droite ou à gauche n'est pas prise en compte, ainsi que les voies d'accélération ou de décélération des échangeurs d'autoroute.");

    // Attribut POS_SOL
    sProduit.createFeatureAttribute(routePrimaire, "POS_SOL", "integer", false);
    AttributeType posSol3 = routePrimaire.getFeatureAttributeByName("POS_SOL");
    posSol3
        .setDefinition("Position par rapport au sol. Donne le niveau de l’objet par rapport à la surface du sol (valeur négative pour un objet souterrain, nulle pour un objet au sol et positive pour un objet en sursol). Si l’objet en sursol passe au dessus d’autres objets en sursol, sa valeur « position par rapport au sol » est égale à « 1 + le nombre d’objets intercalés ». De la même façon, un souterrain peut prendre une valeur « position par rapport au sol » égale à « – 1 – le nombre d’objets souterrains intercalés ».");

    // Attribut SENS
    sProduit.createFeatureAttribute(routePrimaire, "SENS", "string", true);
    AttributeType sens3 = routePrimaire.getFeatureAttributeByName("SENS");
    sens3
        .setDefinition("Sens de circulation autorisée pour les automobiles sur les voies.");
    sProduit.createFeatureAttributeValue(sens3, "Double");
    FC_FeatureAttributeValue double3 = sens3
        .getFeatureAttributeValueByName("Double");
    double3.setDefinition("La circulation est autorisée dans les deux sens.");
    sProduit.createFeatureAttributeValue(sens3, "Direct");
    FC_FeatureAttributeValue direct3 = sens3
        .getFeatureAttributeValueByName("Direct");
    direct3
        .setDefinition("La circulation n’est autorisée que dans le sens de numérisation du tronçon.");
    sProduit.createFeatureAttributeValue(sens3, "Inverse");
    FC_FeatureAttributeValue inverse3 = sens3
        .getFeatureAttributeValueByName("Inverse");
    inverse3
        .setDefinition("La circulation n’est autorisée que dans le sens inverse de numérisation du tronçon.");
    sProduit.createFeatureAttributeValue(sens3, "NC");
    FC_FeatureAttributeValue nc8 = sens3.getFeatureAttributeValueByName("NC");
    nc8.setDefinition("Non concerné.");
    sProduit.createFeatureAttributeValue(sens3, "NR");
    FC_FeatureAttributeValue nr8 = sens3.getFeatureAttributeValueByName("NR");
    nr8.setDefinition("Non renseigné : l’information est manquante dans la base.");

    // Attribut INSEECOM_G
    sProduit.createFeatureAttribute(routePrimaire, "INSEECOM_G", "string",
        false);
    AttributeType inseecom_g3 = routePrimaire
        .getFeatureAttributeByName("INSEECOM_G");
    inseecom_g3
        .setDefinition("INSEE Commune gauche. Numéro d’INSEE de la commune à gauche du tronçon par rapport à son sens de numérisation.");

    // Attribut INSEECOM_D
    sProduit.createFeatureAttribute(routePrimaire, "INSEECOM_D", "string",
        false);
    AttributeType inseecom_d3 = routePrimaire
        .getFeatureAttributeByName("INSEECOM_D");
    inseecom_d3
        .setDefinition("INSEE Commune droite. Numéro d’INSEE de la commune à droite du tronçon par rapport à son sens de numérisation.");

    // Attribut CODEVOIE_G
    sProduit.createFeatureAttribute(routePrimaire, "CODEVOIE_G", "string",
        false);
    AttributeType codevoie_g3 = routePrimaire
        .getFeatureAttributeByName("CODEVOIE_G");
    codevoie_g3
        .setDefinition("Identifiant gauche. Identifiant de la voie associée au côté gauche du tronçon.Il est obtenu par concaténation du numéro d’INSEE (5 caractères) et du code Rivoli (4 caractères) de la voie associée au côté gauche du tronçon.");

    // Attribut CODEVOIE_D
    sProduit.createFeatureAttribute(routePrimaire, "CODEVOIE_D", "string",
        false);
    AttributeType codevoie_d3 = routePrimaire
        .getFeatureAttributeByName("CODEVOIE_D");
    codevoie_d3
        .setDefinition("Identifiant droite. Identifiant de la voie associée au côté droit du tronçon.Il est obtenu par concaténation du numéro d’INSEE (5 caractères) et du code Rivoli (4 caractères) de la voie associée au côté droit du tronçon.");

    // Attribut TYP_ADRES
    sProduit.createFeatureAttribute(routePrimaire, "TYP_ADRES", "string", true);
    AttributeType typAdres3 = routePrimaire
        .getFeatureAttributeByName("TYP_ADRES");
    typAdres3
        .setDefinition("Type d’adressage. Renseigne sur le type d’adressage du tronçon.");
    sProduit.createFeatureAttributeValue(typAdres3, "Classique");
    FC_FeatureAttributeValue clasic3 = typAdres3
        .getFeatureAttributeValueByName("Classique");
    clasic3
        .setDefinition("Un côté de la rue porte des numéros pairs, l’autre des numéros impairs. Les numéros sont ordonnés par ordre croissant ou décroissant le long de la rue.");
    sProduit.createFeatureAttributeValue(typAdres3, "Métrique");
    FC_FeatureAttributeValue metric3 = typAdres3
        .getFeatureAttributeValueByName("Métrique");
    metric3
        .setDefinition("Les numéros des bornes postales correspondent à la distance en mètres qui sépare l’entrée principale de la parcelle d’un point origine arbitraire de la rue. Le principe de côté pair et impair n’est pas toujours conservé.");
    sProduit.createFeatureAttributeValue(typAdres3, "Linéaire");
    FC_FeatureAttributeValue lineaire3 = typAdres3
        .getFeatureAttributeValueByName("Linéaire");
    lineaire3
        .setDefinition("Les numéros sont ordonnés le long de chaque côté de la rue, mais sans distinction pair ou impair.");
    sProduit.createFeatureAttributeValue(typAdres3, "Autre");
    FC_FeatureAttributeValue autre6 = typAdres3
        .getFeatureAttributeValueByName("Autre");
    autre6
        .setDefinition("Ni classique, ni métrique, ni linéaire. Les numéros ne sont pas ordonnés.");
    sProduit.createFeatureAttributeValue(typAdres3, "NC");
    FC_FeatureAttributeValue nc9 = typAdres3
        .getFeatureAttributeValueByName("NC");
    nc9.setDefinition("Non concerné.");
    sProduit.createFeatureAttributeValue(typAdres3, "NR");
    FC_FeatureAttributeValue nr9 = typAdres3
        .getFeatureAttributeValueByName("NR");
    nr9.setDefinition("Non renseigné.");

    // Attribut BORNEDEB_G
    sProduit.createFeatureAttribute(routePrimaire, "BORNEDEB_G", "integer",
        false);
    AttributeType bornedeb_g3 = routePrimaire
        .getFeatureAttributeByName("BORNEDEB_G");
    bornedeb_g3
        .setDefinition("Borne début gauche. Numéro de borne à gauche du tronçon en son sommet initial.");

    // Attribut BORNEDEB_D
    sProduit.createFeatureAttribute(routePrimaire, "BORNEDEB_D", "integer",
        false);
    AttributeType bornedeb_d3 = routePrimaire
        .getFeatureAttributeByName("BORNEDEB_D");
    bornedeb_d3
        .setDefinition("Borne début droite. Numéro de borne à droite du tronçon en son sommet initial.");

    // Attribut BORNEFIN_G
    sProduit.createFeatureAttribute(routePrimaire, "BORNEFIN_G", "integer",
        false);
    AttributeType bornefin_g3 = routePrimaire
        .getFeatureAttributeByName("BORNEFIN_G");
    bornefin_g3
        .setDefinition("Borne fin gauche. Numéro de borne à gauche du tronçon en son sommet final.");

    // Attribut BORNEFIN_D
    sProduit.createFeatureAttribute(routePrimaire, "BORNEFIN_D", "integer",
        false);
    AttributeType bornefin_d3 = routePrimaire
        .getFeatureAttributeByName("BORNEFIN_D");
    bornefin_d3
        .setDefinition("Borne fin droite. Numéro de borne à droite du tronçon en son sommet final.");

    // Attribut ETAT
    sProduit.createFeatureAttribute(routePrimaire, "ETAT", "string", false);
    AttributeType etat3 = routePrimaire.getFeatureAttributeByName("ETAT");
    etat3.setDefinition("Etat du tronçon.");

    // Attribut Z_INI
    sProduit.createFeatureAttribute(routePrimaire, "Z_INI", "float", false);
    AttributeType zini3 = routePrimaire.getFeatureAttributeByName("Z_INI");
    zini3
        .setDefinition("Altitude initiale : c’est l’altitude du sommet initial du tronçon.");

    // Attribut Z_FIN
    sProduit.createFeatureAttribute(routePrimaire, "Z_FIN", "float", false);
    AttributeType zfin3 = routePrimaire.getFeatureAttributeByName("Z_FIN");
    zfin3
        .setDefinition("Altitude finale : c’est l’altitude du sommet final du tronçon.");

    // Classe Route
    // secondaire///////////////////////////////////////////////////

    sProduit.createFeatureType("ROUTE_SECONDAIRE");
    FeatureType routeSecondaire = (FeatureType) (sProduit
        .getFeatureTypeByName("ROUTE_SECONDAIRE"));
    routeSecondaire
        .setDefinition("Portion de voie de communication destinée aux automobiles, aux piétons, aux cycles ou aux animaux, homogène pour l'ensemble des attributs et des relations qui la concernent. Cette classe est un sous-ensemble de la classe ROUTE, et comprend uniquement les tronçons de route d’importance supérieure à 2. Cela permet de n’utiliser ou de n’afficher que le réseau dit secondaire, pour des raisons de faciliter de manipulation ou de lisibilité à l’écran suivant l’échelle.");
    routeSecondaire.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(routeSecondaire, "ID", "string", false);
    AttributeType id4 = routeSecondaire.getFeatureAttributeByName("ID");
    id4.setDefinition("Identifiant Tronçon.Cet identifiant est unique. Il est stable d’une édition à l’autre. Il permet aussi d’établir un lien entre le ponctuel de la classe « ADRESSE » des produits BD ADRESSE® et POINT ADRESSE® (par l’intermédiaire de l’attribut ID_TR) et l’objet linéaire de la classe « ROUTE ».");

    // Attribut PREC_PLANI
    sProduit.createFeatureAttribute(routeSecondaire, "PREC_PLANI", "float",
        true);
    AttributeType prec_plani4 = routeSecondaire
        .getFeatureAttributeByName("PREC_PLANI");
    prec_plani4
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani4, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani4, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani4, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani4, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani4, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani4, "30.0");

    // Attribut PREC_ALTI
    sProduit
        .createFeatureAttribute(routeSecondaire, "PREC_ALTI", "float", true);
    AttributeType prec_alti4 = routeSecondaire
        .getFeatureAttributeByName("PREC_ALTI");
    prec_alti4
        .setDefinition("Précision géométrique altimétrique. Attribut précisant la précision géométrique en altimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_alti4, "1.0");
    sProduit.createFeatureAttributeValue(prec_alti4, "2.5");
    sProduit.createFeatureAttributeValue(prec_alti4, "20.0");
    sProduit.createFeatureAttributeValue(prec_alti4, "9999.0");

    // Attribut NATURE
    sProduit.createFeatureAttribute(routeSecondaire, "NATURE", "string", true);
    AttributeType nature4 = routeSecondaire.getFeatureAttributeByName("NATURE");
    nature4
        .setDefinition("Attribut permettant de distinguer différentes natures de tronçon de route.");
    sProduit.createFeatureAttributeValue(nature4, "Autoroute");
    FC_FeatureAttributeValue autoroute7 = nature4
        .getFeatureAttributeValueByName("Autoroute");
    autoroute7
        .setDefinition("Routes sans croisement, accessibles seulement en des points aménagés à cet effet et réservées aux véhicules à propulsion mécanique. Le classement dans la catégorie des autoroutes est prononcé par décret du conseil d’état, pris après enquête publique.");
    sProduit.createFeatureAttributeValue(nature4, "Quasi-autoroute");
    FC_FeatureAttributeValue quasiautoroute5 = nature4
        .getFeatureAttributeValueByName("Quasi-autoroute");
    quasiautoroute5
        .setDefinition("Routes de même définition que l’autoroute mais non classées officiellement dans cette catégorie. Ce sont des routes à chaussées séparées par un terre-plein central, qui ne possèdent pas de croisement à niveau avec le reste du réseau routier.");
    sProduit.createFeatureAttributeValue(nature4, "Bretelle");
    FC_FeatureAttributeValue bretelle5 = nature4
        .getFeatureAttributeValueByName("Bretelle");
    bretelle5
        .setDefinition("Bretelles de liaison (ou d’échangeur) ou voies d’accès à une Aire de Service ou de Repos.");
    sProduit.createFeatureAttributeValue(nature4, "Route à 2 chaussées");
    FC_FeatureAttributeValue routeA2Chaussees5 = nature4
        .getFeatureAttributeValueByName("Route à 2 chaussées");
    routeA2Chaussees5
        .setDefinition("Routes comportant 2 chaussées séparées par un obstacle physique éventuellement ouvert aux carrefours. Elles possèdent donc des croisements à niveau, ce qui leur interdit d'être classées dans la catégorie Autoroute ou Quasi-autoroute.");
    sProduit.createFeatureAttributeValue(nature4, "Route à 1 chaussée");
    FC_FeatureAttributeValue routeA1Chaussee5 = nature4
        .getFeatureAttributeValueByName("Route à 1 chaussée");
    routeA1Chaussee5
        .setDefinition("Routes comportant 1 chaussée.Toutes les routes goudronnées qui ne sont pas classées en Route à 2 chaussées, Quasi-autoroute ou Autoroute se retrouvent dans cette classe.");
    sProduit.createFeatureAttributeValue(nature4, "Route empierrée");
    FC_FeatureAttributeValue routeEmpierree5 = nature4
        .getFeatureAttributeValueByName("Route empierrée");
    routeEmpierree5
        .setDefinition("Routes sommairement revêtues (pas de revêtement de surface ou revêtement très dégradé), mais permettant la circulation de véhicules automobiles de tourisme par tout temps. Toutes les routes empierrées sont incluses.");
    sProduit.createFeatureAttributeValue(nature4, "Chemin");
    FC_FeatureAttributeValue chemin7 = nature4
        .getFeatureAttributeValueByName("Chemin");
    chemin7
        .setDefinition("Les chemins sont prévus pour la circulation de véhicules ou d’engins d’exploitation. Ils ne sont pas forcément carrossables pour tous les véhicules et par tout temps (voir aussi “route empierrée”).");
    sProduit.createFeatureAttributeValue(nature4, "Bac auto");
    FC_FeatureAttributeValue bacAuto5 = nature4
        .getFeatureAttributeValueByName("Bac auto");
    bacAuto5
        .setDefinition("Trajets du bateau servant à passer des véhicules d’une rive à l’autre d’un cours d’eau ou d’un bras de mer.");
    sProduit.createFeatureAttributeValue(nature4, "Bac piéton");
    FC_FeatureAttributeValue bacPieton5 = nature4
        .getFeatureAttributeValueByName("Bac piéton");
    bacPieton5
        .setDefinition("Trajets du bateau servant à passer des piétons d’une rive à l’autre d’un cours d’eau ou d’un bras de mer.");
    sProduit.createFeatureAttributeValue(nature4, "Piste cyclable");
    FC_FeatureAttributeValue pisteCyclable7 = nature4
        .getFeatureAttributeValueByName("Piste cyclable");
    pisteCyclable7
        .setDefinition("Tronçons de chaussée en site propre, réservée aux cycles et cyclomoteurs. La longueur doit être supérieure à 200m. Les bandes cyclables sont exclues.");
    sProduit.createFeatureAttributeValue(nature4, "Sentier");
    FC_FeatureAttributeValue sentier7 = nature4
        .getFeatureAttributeValueByName("Sentier");
    sentier7
        .setDefinition("Chemins étroits ne permettant pas le passage de véhicules. Seuls les principaux sentiers sont inclus. Passerelles supportant une allée, directement reliés au réseau routier. Les passerelles ont une position par rapport au sol supérieure à 0.");
    sProduit.createFeatureAttributeValue(nature4, "Escalier");
    FC_FeatureAttributeValue escalier7 = nature4
        .getFeatureAttributeValueByName("Escalier");
    escalier7
        .setDefinition("Escaliers directement reliés au réseau routier supportant une allée, assurant la jonction entre deux voies de communication ou entre le réseau routier et un élément adressable. Sur rue, les escaliers visibles sur les photographies aériennes sont distingués quelle que soit leur longueur.");

    // Attribut NUMERO
    sProduit.createFeatureAttribute(routeSecondaire, "NUMERO", "string", false);
    AttributeType numero4 = routeSecondaire.getFeatureAttributeByName("NUMERO");
    numero4
        .setDefinition("Désigne le classement administratif d'un tronçon routier.");

    // Attribut NOM_RUE_G
    sProduit.createFeatureAttribute(routeSecondaire, "NOM_RUE_G", "string",
        false);
    AttributeType nomRueG4 = routeSecondaire
        .getFeatureAttributeByName("NOM_RUE_G");
    nomRueG4
        .setDefinition("Nom rue gauche. Une rue est un ensemble de tronçons de route associés à un même nom. Une rue est identifiée par son nom dans une commune donnée. « NOM_RUE_G » est le nom porté par la rue pour la commune dont le numéro INSEE est identique à la valeur de l’attribut « INSEECOM_G ». Dans les résidences portant un nom avec des allées comportant également des noms, on garde le nom servant à l’adressage. Dans certains cas le toponyme qui sert à l’adressage est un nom de lieu-dit. Il servira à nommer la voie qui dessert ces adresses. Les lettres « LD » placées devant le toponyme serviront à les identifier.");

    // Attribut NOM_RUE_D
    sProduit.createFeatureAttribute(routeSecondaire, "NOM_RUE_D", "string",
        false);
    AttributeType nomRueD4 = routeSecondaire
        .getFeatureAttributeByName("NOM_RUE_D");
    nomRueD4
        .setDefinition("Nom rue droite. Une rue est un ensemble de tronçons de route associés à un même nom. Une rue est identifiée par son nom dans une commune donnée. « NOM_RUE_D » est le nom porté par la rue pour la commune dont le numéro INSEE est identique à la valeur de l’attribut « INSEECOM_D ». Dans les résidences portant un nom avec des allées comportant également des noms, on garde le nom servant à l’adressage. Dans certains cas le toponyme qui sert à l’adressage est un nom de lieu-dit. Il servira à nommer la voie qui dessert ces adresses. Les lettres « LD » placées devant le toponyme serviront à les identifier.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(routeSecondaire, "IMPORTANCE", "string",
        true);
    AttributeType importance4 = routeSecondaire
        .getFeatureAttributeByName("IMPORTANCE");
    importance4
        .setDefinition("Cet attribut matérialise une hiérarchisation du réseau routier fondée, non pas sur un critère administratif, mais sur l'importance des tronçons de route pour le trafic routier. Ainsi, les valeurs \"1\", \"2\", \"3\", \"4\", \"5\" permettent un maillage de plus en plus dense du territoire. Le graphe des éléments appartenant à un degré (autre que le plus bas) et aux niveaux supérieurs est connexe.");
    sProduit.createFeatureAttributeValue(importance4, "1");
    FC_FeatureAttributeValue un4 = importance4
        .getFeatureAttributeValueByName("1");
    un4.setDefinition("Le réseau 1 assure les liaisons entre métropoles et compose l’essentiel du réseau européen. Il est composé en général d’autoroutes et quasi-autoroutes, parfois de nationales.");
    sProduit.createFeatureAttributeValue(importance4, "2");
    FC_FeatureAttributeValue deux4 = importance4
        .getFeatureAttributeValueByName("2");
    deux4
        .setDefinition("Liaisons entre départements. Cette valeur représente une densification du maillage routier défini par les tronçons d’importance 1. Les liaisons d’importance 2 ont fonction d’assurer les liaisons à fort trafic à caractère prioritaire entre agglomérations importantes, d’assurer les liaisons des agglomérations importantes au réseau d’importance 1, d’offrir une alternative à une autoroute si celle-ci est payante, de proposer des itinéraires de contournement des agglomérations, d’assurer la continuité, en agglomération, des liaisons interurbaines à fort trafic quand il n’y a pas de contournement possible.");
    sProduit.createFeatureAttributeValue(importance4, "3");
    FC_FeatureAttributeValue trois4 = importance4
        .getFeatureAttributeValueByName("3");
    trois4
        .setDefinition("Liaisons ville à ville à l’intérieur d’un département. Ce niveau est majoritairement représenté par des routes départementales, toutefois certaines départementales peuvent avoir une importance 4 ou 5. Cette valeur représente une densification du maillage routier défini par les tronçons d’importance 2. Les liaisons d’importance 3 ont fonction de relier les communes de moindre importance entre elles (les chefs-lieux de canton en particulier), de desservir les localités et sites touristiques importants, de desservir les points de passage des obstacles naturels quand ils sont peu nombreux (cols routiers, ponts), de desservir les agglomérations d'où partent des liaisons maritimes, de structurer la circulation en agglomération.");
    sProduit.createFeatureAttributeValue(importance4, "4");
    FC_FeatureAttributeValue quatre4 = importance4
        .getFeatureAttributeValueByName("4");
    quatre4
        .setDefinition("Voies permettant de se déplacer rapidement à l’intérieur d’une commune et, dans les zones rurales, de relier le bourg aux hameaux proches. Cette valeur représente une densification du maillage routier défini par les tronçons d’importance 3. Les liaisons d’importance 4 ont fonction de structurer la circulation en agglomération, de relier le bourg aux hameaux proches.");
    sProduit.createFeatureAttributeValue(importance4, "5");
    FC_FeatureAttributeValue cinq4 = importance4
        .getFeatureAttributeValueByName("5");
    cinq4
        .setDefinition("Voies permettant de desservir l’intérieur d’une commune. Valeur prise par exclusion des autres valeurs de l'attribut.");
    sProduit.createFeatureAttributeValue(importance4, "NC");
    FC_FeatureAttributeValue nc4bis = importance4
        .getFeatureAttributeValueByName("NC");
    nc4bis.setDefinition("Non concerné par cet attribut.");
    sProduit.createFeatureAttributeValue(importance4, "NR");
    FC_FeatureAttributeValue nr4bis = importance4
        .getFeatureAttributeValueByName("NR");
    nr4bis.setDefinition("Non renseigné.");

    // Attribut CL_ADMIN
    sProduit
        .createFeatureAttribute(routeSecondaire, "CL_ADMIN", "string", true);
    AttributeType clAdmin4 = routeSecondaire
        .getFeatureAttributeByName("CL_ADMIN");
    clAdmin4
        .setDefinition("Classement administratif. Attribut précisant le statut d'une route numérotée ou nommée.");
    sProduit.createFeatureAttributeValue(clAdmin4, "Autoroute");
    FC_FeatureAttributeValue autoroute6 = clAdmin4
        .getFeatureAttributeValueByName("Autoroute");
    autoroute6
        .setDefinition("Les autoroutes sont des routes sans croisement, accessibles seulement en des points aménagés à cet effet et réservées aux véhicules à propulsion mécanique (Article L122-1 du code de la voirie routière).");
    sProduit.createFeatureAttributeValue(clAdmin4, "Nationale");
    FC_FeatureAttributeValue nationale6 = clAdmin4
        .getFeatureAttributeValueByName("Nationale");
    nationale6
        .setDefinition("Route nationale (voies du domaine public routier national autres que les autoroutes précédemment définies).");
    sProduit.createFeatureAttributeValue(clAdmin4, "Départementale");
    FC_FeatureAttributeValue departementale6 = clAdmin4
        .getFeatureAttributeValueByName("Départementale");
    departementale6
        .setDefinition("Voie qui fait partie du domaine public routier départemental.");
    sProduit.createFeatureAttributeValue(clAdmin4, "Autre");
    FC_FeatureAttributeValue autre8 = clAdmin4
        .getFeatureAttributeValueByName("Autre");
    autre8
        .setDefinition("Toute autre voie non classée dans les catégories administratives précédentes.");

    // Attribut GESTION
    sProduit
        .createFeatureAttribute(routeSecondaire, "GESTION", "string", false);
    AttributeType gestion4 = routeSecondaire
        .getFeatureAttributeByName("GESTION");
    gestion4
        .setDefinition("Gestionnaire. Définit le gestionnaire administratif d’une route. Toutes les routes classées possèdent un ‘Gestionnaire’. Il existe différentes catégories de routes pour lesquelles le gestionnaire diffère : pour les routes départementales, il s’agit du gestionnaire départemental de la route au sens administratif (c’est-à-dire le numéro de département),  pour les routes nationales et les autoroutes non concédées, le gestionnaire correspond également au gestionnaire départemental de la route (au sens administratif), pour les autoroutes concédées, le gestionnaire est la société concessionnaire d’autoroute (une correspondance est établie entre ces sociétés et un code en trois lettres), les routes codées sur les bretelles d’échangeurs identifiés prennent le gestionnaire de la route à laquelle l’échangeur est rattaché.");
    // Serait complétable au niveau des valeurs énumérées moyennant beaucoup
    // de
    // patience

    // Attribut MISE_SERV
    sProduit.createFeatureAttribute(routeSecondaire, "MISE_SERV", "string",
        false);
    AttributeType miseServ4 = routeSecondaire
        .getFeatureAttributeByName("MISE_SERV");
    miseServ4
        .setDefinition("Date de mise en service. Définit la date prévue ou la date effective de mise en service d’un tronçon de route. Cet attribut n'est rempli que pour les tronçons en construction, il est à “1000-01-01“ dans les autres cas. Les tronçons qui possèdent une date de mise en service sont complètement fermés aux véhicules avant cette date.");

    // Attribut IT_VERT
    sProduit
        .createFeatureAttribute(routeSecondaire, "IT_VERT", "string", false);
    AttributeType itVert4 = routeSecondaire
        .getFeatureAttributeByName("IT_VERT");
    itVert4
        .setDefinition("Itinéraire vert. Indique l’appartenance ou non d’un tronçon routier au réseau vert. Le réseau vert, composé de pôles verts et de liaisons vertes, couvre l’ensemble du territoire français.Les pôles verts sont composés de communes de plus de 23.000 habitants en province et de 39.000 habitants en Ile-de-France, ainsi que certains pôles d’activités administratifs, économiques, touristiques ou industriels. On retient seulement le réseau vert de transit entre pôles verts. Le réseau vert de rabattement, à l’intérieur des villes, et le réseau vert conseillé aux poids lourds ne sont pas retenus.");

    // Attribut IT_EUROP
    sProduit.createFeatureAttribute(routeSecondaire, "IT_EUROP", "string",
        false);
    AttributeType itEurop4 = routeSecondaire
        .getFeatureAttributeByName("IT_EUROP");
    itEurop4
        .setDefinition("Itinéraire européen. Numéro de route européenne : une route européenne emprunte en général le réseau autoroutier ou national (exceptionnellement départemental ou non classé).");

    // Attribut FICTIF
    sProduit.createFeatureAttribute(routeSecondaire, "FICTIF", "string", false);
    AttributeType fictif4 = routeSecondaire.getFeatureAttributeByName("FICTIF");
    fictif4
        .setDefinition("La valeur “oui“ indique que la géométrie du tronçon de route n'est pas significative. La présence de ce dernier sert à raccorder une bretelle à l’axe d’une chaussée afin d'assurer la continuité du réseau routier linéaire.");
    sProduit.createFeatureAttributeValue(fictif4, "Oui");
    sProduit.createFeatureAttributeValue(fictif4, "Non");

    // Attribut FRANCHISSMT
    sProduit.createFeatureAttribute(routeSecondaire, "FRANCHISSMT", "string",
        true);
    AttributeType franchissmt4 = routeSecondaire
        .getFeatureAttributeByName("FRANCHISSMT");
    franchissmt4
        .setDefinition("Franchissement.Cet attribut informe sur le niveau de l’objet par rapport à la surface du sol.");
    sProduit.createFeatureAttributeValue(franchissmt4, "Gué ou radier");
    FC_FeatureAttributeValue gue4 = franchissmt4
        .getFeatureAttributeValueByName("Gué ou radier");
    gue4.setDefinition("Passage naturel ou aménagé permettant aux véhicules de traverser un cours d’eau sans le recours d’un pont ou d’un bateau.");
    sProduit.createFeatureAttributeValue(franchissmt4, "Pont");
    FC_FeatureAttributeValue pont4 = franchissmt4
        .getFeatureAttributeValueByName("Pont");
    pont4
        .setDefinition("Tronçon de route situé au-dessus du niveau du sol (Ponceau, Pont, Pont mobile, Viaduc, Passerelle).");
    sProduit.createFeatureAttributeValue(franchissmt4, "Tunnel");
    FC_FeatureAttributeValue tunnel4 = franchissmt4
        .getFeatureAttributeValueByName("Tunnel");
    tunnel4
        .setDefinition("Tronçon de route situé sous le niveau du sol (Tunnel).");
    sProduit.createFeatureAttributeValue(franchissmt4, "NC");
    FC_FeatureAttributeValue nc10 = franchissmt4
        .getFeatureAttributeValueByName("NC");
    nc10.setDefinition("Tronçon de route situé au niveau du sol (y compris les tronçons en déblai et en remblai).");

    // Attribut Largeur
    sProduit.createFeatureAttribute(routeSecondaire, "LARGEUR", "float", false);
    AttributeType largeur4 = routeSecondaire
        .getFeatureAttributeByName("LARGEUR");
    largeur4
        .setDefinition("Largeur de chaussée. Largeur de chaussée (d’accotement à accotement) exprimée en mètres.");

    // Attribut NOM_ITI
    sProduit
        .createFeatureAttribute(routeSecondaire, "NOM_ITI", "string", false);
    AttributeType nomIti4 = routeSecondaire
        .getFeatureAttributeByName("NOM_ITI");
    nomIti4
        .setDefinition("Nom d’itinéraire. Définit un parcours routier nommé.");

    // Attribut NB_VOIES
    sProduit.createFeatureAttribute(routeSecondaire, "NB_VOIES", "integer",
        false);
    AttributeType nbVoies4 = routeSecondaire
        .getFeatureAttributeByName("NB_VOIES");
    nbVoies4
        .setDefinition("Nombre de voies.Nombre total de voies d’une route, d’une rue ou d’une chaussée de route à chaussées séparées.Lorsque les voies ne sont pas matérialisées, l’attribut indique le nombre maximum de voies de circulation effectivement utilisées dans des conditions normales de circulation.L'augmentation du nombre de voies au niveau d'un carrefour pour permettre de tourner plus facilement à droite ou à gauche n'est pas prise en compte, ainsi que les voies d'accélération ou de décélération des échangeurs d'autoroute.");

    // Attribut POS_SOL
    sProduit.createFeatureAttribute(routeSecondaire, "POS_SOL", "integer",
        false);
    AttributeType posSol4 = routeSecondaire
        .getFeatureAttributeByName("POS_SOL");
    posSol4
        .setDefinition("Position par rapport au sol. Donne le niveau de l’objet par rapport à la surface du sol (valeur négative pour un objet souterrain, nulle pour un objet au sol et positive pour un objet en sursol). Si l’objet en sursol passe au dessus d’autres objets en sursol, sa valeur « position par rapport au sol » est égale à « 1 + le nombre d’objets intercalés ». De la même façon, un souterrain peut prendre une valeur « position par rapport au sol » égale à « – 1 – le nombre d’objets souterrains intercalés ».");

    // Attribut SENS
    sProduit.createFeatureAttribute(routeSecondaire, "SENS", "string", true);
    AttributeType sens4 = routeSecondaire.getFeatureAttributeByName("SENS");
    sens4
        .setDefinition("Sens de circulation autorisée pour les automobiles sur les voies.");
    sProduit.createFeatureAttributeValue(sens4, "Double");
    FC_FeatureAttributeValue double4 = sens4
        .getFeatureAttributeValueByName("Double");
    double4.setDefinition("La circulation est autorisée dans les deux sens.");
    sProduit.createFeatureAttributeValue(sens4, "Direct");
    FC_FeatureAttributeValue direct4 = sens4
        .getFeatureAttributeValueByName("Direct");
    direct4
        .setDefinition("La circulation n’est autorisée que dans le sens de numérisation du tronçon.");
    sProduit.createFeatureAttributeValue(sens4, "Inverse");
    FC_FeatureAttributeValue inverse4 = sens4
        .getFeatureAttributeValueByName("Inverse");
    inverse4
        .setDefinition("La circulation n’est autorisée que dans le sens inverse de numérisation du tronçon.");
    sProduit.createFeatureAttributeValue(sens4, "NC");
    FC_FeatureAttributeValue nc11 = sens4.getFeatureAttributeValueByName("NC");
    nc11.setDefinition("Non concerné.");
    sProduit.createFeatureAttributeValue(sens4, "NR");
    FC_FeatureAttributeValue nr11 = sens4.getFeatureAttributeValueByName("NR");
    nr11.setDefinition("Non renseigné : l’information est manquante dans la base.");

    // Attribut INSEECOM_G
    sProduit.createFeatureAttribute(routeSecondaire, "INSEECOM_G", "string",
        false);
    AttributeType inseecom_g4 = routeSecondaire
        .getFeatureAttributeByName("INSEECOM_G");
    inseecom_g4
        .setDefinition("INSEE Commune gauche. Numéro d’INSEE de la commune à gauche du tronçon par rapport à son sens de numérisation.");

    // Attribut INSEECOM_D
    sProduit.createFeatureAttribute(routeSecondaire, "INSEECOM_D", "string",
        false);
    AttributeType inseecom_d4 = routeSecondaire
        .getFeatureAttributeByName("INSEECOM_D");
    inseecom_d4
        .setDefinition("INSEE Commune droite. Numéro d’INSEE de la commune à droite du tronçon par rapport à son sens de numérisation.");

    // Attribut CODEVOIE_G
    sProduit.createFeatureAttribute(routeSecondaire, "CODEVOIE_G", "string",
        false);
    AttributeType codevoie_g4 = routeSecondaire
        .getFeatureAttributeByName("CODEVOIE_G");
    codevoie_g4
        .setDefinition("Identifiant gauche. Identifiant de la voie associée au côté gauche du tronçon.Il est obtenu par concaténation du numéro d’INSEE (5 caractères) et du code Rivoli (4 caractères) de la voie associée au côté gauche du tronçon.");

    // Attribut CODEVOIE_D
    sProduit.createFeatureAttribute(routeSecondaire, "CODEVOIE_D", "string",
        false);
    AttributeType codevoie_d4 = routeSecondaire
        .getFeatureAttributeByName("CODEVOIE_D");
    codevoie_d4
        .setDefinition("Identifiant droite. Identifiant de la voie associée au côté droit du tronçon.Il est obtenu par concaténation du numéro d’INSEE (5 caractères) et du code Rivoli (4 caractères) de la voie associée au côté droit du tronçon.");

    // Attribut TYP_ADRES
    sProduit.createFeatureAttribute(routeSecondaire, "TYP_ADRES", "string",
        true);
    AttributeType typAdres4 = routeSecondaire
        .getFeatureAttributeByName("TYP_ADRES");
    typAdres4
        .setDefinition("Type d’adressage. Renseigne sur le type d’adressage du tronçon.");
    sProduit.createFeatureAttributeValue(typAdres4, "Classique");
    FC_FeatureAttributeValue clasic4 = typAdres4
        .getFeatureAttributeValueByName("Classique");
    clasic4
        .setDefinition("Un côté de la rue porte des numéros pairs, l’autre des numéros impairs. Les numéros sont ordonnés par ordre croissant ou décroissant le long de la rue.");
    sProduit.createFeatureAttributeValue(typAdres4, "Métrique");
    FC_FeatureAttributeValue metric4 = typAdres4
        .getFeatureAttributeValueByName("Métrique");
    metric4
        .setDefinition("Les numéros des bornes postales correspondent à la distance en mètres qui sépare l’entrée principale de la parcelle d’un point origine arbitraire de la rue. Le principe de côté pair et impair n’est pas toujours conservé.");
    sProduit.createFeatureAttributeValue(typAdres4, "Linéaire");
    FC_FeatureAttributeValue lineaire4 = typAdres4
        .getFeatureAttributeValueByName("Linéaire");
    lineaire4
        .setDefinition("Les numéros sont ordonnés le long de chaque côté de la rue, mais sans distinction pair ou impair.");
    sProduit.createFeatureAttributeValue(typAdres4, "Autre");
    FC_FeatureAttributeValue autre7 = typAdres4
        .getFeatureAttributeValueByName("Autre");
    autre7
        .setDefinition("Ni classique, ni métrique, ni linéaire. Les numéros ne sont pas ordonnés.");
    sProduit.createFeatureAttributeValue(typAdres4, "NC");
    FC_FeatureAttributeValue nc12 = typAdres4
        .getFeatureAttributeValueByName("NC");
    nc12.setDefinition("Non concerné.");
    sProduit.createFeatureAttributeValue(typAdres4, "NR");
    FC_FeatureAttributeValue nr12 = typAdres4
        .getFeatureAttributeValueByName("NR");
    nr12.setDefinition("Non renseigné.");

    // Attribut BORNEDEB_G
    sProduit.createFeatureAttribute(routeSecondaire, "BORNEDEB_G", "integer",
        false);
    AttributeType bornedeb_g4 = routeSecondaire
        .getFeatureAttributeByName("BORNEDEB_G");
    bornedeb_g4
        .setDefinition("Borne début gauche. Numéro de borne à gauche du tronçon en son sommet initial.");

    // Attribut BORNEDEB_D
    sProduit.createFeatureAttribute(routeSecondaire, "BORNEDEB_D", "integer",
        false);
    AttributeType bornedeb_d4 = routeSecondaire
        .getFeatureAttributeByName("BORNEDEB_D");
    bornedeb_d4
        .setDefinition("Borne début droite. Numéro de borne à droite du tronçon en son sommet initial.");

    // Attribut BORNEFIN_G
    sProduit.createFeatureAttribute(routeSecondaire, "BORNEFIN_G", "integer",
        false);
    AttributeType bornefin_g4 = routeSecondaire
        .getFeatureAttributeByName("BORNEFIN_G");
    bornefin_g4
        .setDefinition("Borne fin gauche. Numéro de borne à gauche du tronçon en son sommet final.");

    // Attribut BORNEFIN_D
    sProduit.createFeatureAttribute(routeSecondaire, "BORNEFIN_D", "integer",
        false);
    AttributeType bornefin_d4 = routeSecondaire
        .getFeatureAttributeByName("BORNEFIN_D");
    bornefin_d4
        .setDefinition("Borne fin droite. Numéro de borne à droite du tronçon en son sommet final.");

    // Attribut ETAT
    sProduit.createFeatureAttribute(routeSecondaire, "ETAT", "string", false);
    AttributeType etat4 = routeSecondaire.getFeatureAttributeByName("ETAT");
    etat4.setDefinition("Etat du tronçon.");

    // Attribut Z_INI
    sProduit.createFeatureAttribute(routeSecondaire, "Z_INI", "float", false);
    AttributeType zini4 = routeSecondaire.getFeatureAttributeByName("Z_INI");
    zini4
        .setDefinition("Altitude initiale : c’est l’altitude du sommet initial du tronçon.");

    // Attribut Z_FIN
    sProduit.createFeatureAttribute(routeSecondaire, "Z_FIN", "float", false);
    AttributeType zfin4 = routeSecondaire.getFeatureAttributeByName("Z_FIN");
    zfin4
        .setDefinition("Altitude finale : c’est l’altitude du sommet final du tronçon.");

    // Classe Surface de
    // route///////////////////////////////////////////////////

    sProduit.createFeatureType("SURFACE_ROUTE");
    FeatureType surfaceRoute = (FeatureType) (sProduit
        .getFeatureTypeByName("SURFACE_ROUTE"));
    surfaceRoute
        .setDefinition("Partie de la chaussée d’une route caractérisée par une largeur exceptionnelle (place, carrefour, péage, parking). Zone à trafic non structuré.");
    surfaceRoute.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(surfaceRoute, "ID", "string", false);
    AttributeType id7 = surfaceRoute.getFeatureAttributeByName("ID");
    id7.setDefinition("Identifiant de la surface.Cet identifiant est unique. Il est stable d’une édition à l’autre. ");

    // Attribut PREC_PLANI
    sProduit.createFeatureAttribute(surfaceRoute, "PREC_PLANI", "float", true);
    AttributeType prec_plani6 = surfaceRoute
        .getFeatureAttributeByName("PREC_PLANI");
    prec_plani6
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani6, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani6, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani6, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani6, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani6, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani6, "30.0");

    // Attribut PREC_ALTI
    sProduit.createFeatureAttribute(surfaceRoute, "PREC_ALTI", "float", true);
    AttributeType prec_alti6 = surfaceRoute
        .getFeatureAttributeByName("PREC_ALTI");
    prec_alti6
        .setDefinition("Précision géométrique altimétrique. Attribut précisant la précision géométrique en altimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_alti6, "1.0");
    sProduit.createFeatureAttributeValue(prec_alti6, "2.5");
    sProduit.createFeatureAttributeValue(prec_alti6, "20.0");
    sProduit.createFeatureAttributeValue(prec_alti6, "9999.0");

    // Attribut NATURE
    sProduit.createFeatureAttribute(surfaceRoute, "NATURE", "string", true);
    AttributeType nature6 = surfaceRoute.getFeatureAttributeByName("NATURE");
    nature6
        .setDefinition("Attribut permettant de distinguer différentes natures de surface de route.");
    sProduit.createFeatureAttributeValue(nature6, "Parking");
    FC_FeatureAttributeValue parking = nature6
        .getFeatureAttributeValueByName("Parking");
    parking
        .setDefinition("Parking non couvert, public ou privé d’environ un demi hectare et plus.Aire de repos, aire de service, aire de stationnement.");
    sProduit.createFeatureAttributeValue(nature6, "Péage");
    FC_FeatureAttributeValue peage = nature6
        .getFeatureAttributeValueByName("Péage");
    peage.setDefinition("Aire de péage (emprise de la chaussée).");
    sProduit.createFeatureAttributeValue(nature6, "Place ou carrefour");
    FC_FeatureAttributeValue place = nature6
        .getFeatureAttributeValueByName("Place ou carrefour");
    place.setDefinition("Place ou carrefour revêtu de grande largeur.");

    // Attribut Z_MOYEN
    sProduit.createFeatureAttribute(surfaceRoute, "Z_MOYEN", "float", false);
    AttributeType zmoy = surfaceRoute.getFeatureAttributeByName("Z_MOYEN");
    zmoy.setDefinition("Altitude moyenne des points composants la géométrie de l’objet telle qu’il a été saisi à l’origine lorsqu’il est issu d’une saisie photogrammétrique.");

    // Classe
    // Toponyme_communication///////////////////////////////////////////////////

    sProduit.createFeatureType("TOPONYME_COMMUNICATION");
    FeatureType topoCom = (FeatureType) (sProduit
        .getFeatureTypeByName("TOPONYME_COMMUNICATION"));
    topoCom.setDefinition("Objet nommé du thème routier.");
    topoCom.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(topoCom, "ID", "string", false);
    AttributeType id8 = topoCom.getFeatureAttributeByName("ID");
    id8.setDefinition("Identifiant du toponyme communication.Cet identifiant est unique. Il est stable d’une édition à l’autre. ");

    // Attribut ORIGIN_NOM
    sProduit.createFeatureAttribute(topoCom, "ORIGINE_NOM", "string", true);
    AttributeType origin = topoCom.getFeatureAttributeByName("ORIGINE_NOM");
    origin
        .setDefinition("Origine du toponyme. Attribut précisant l’origine de la donnée.");
    sProduit.createFeatureAttributeValue(origin, "Scan25");
    FC_FeatureAttributeValue scan = origin
        .getFeatureAttributeValueByName("Scan25");
    scan.setDefinition("Carte IGN au 1 : 25 000.");
    sProduit.createFeatureAttributeValue(origin, "BDCarto");
    FC_FeatureAttributeValue bdcarto = origin
        .getFeatureAttributeValueByName("BDCarto");
    bdcarto
        .setDefinition("Base de données BD CARTO® pour la géométrie de l’objet.");
    sProduit.createFeatureAttributeValue(origin, "BDTopo");
    FC_FeatureAttributeValue bdtopo = origin
        .getFeatureAttributeValueByName("BDTopo");
    bdtopo
        .setDefinition("Base de données BD TOPO® antérieure à la BD TOPO® Pays.");
    sProduit.createFeatureAttributeValue(origin, "BDNyme");
    FC_FeatureAttributeValue bdnyme = origin
        .getFeatureAttributeValueByName("BDNyme");
    bdnyme.setDefinition("Base de données BD NYME®.");
    sProduit.createFeatureAttributeValue(origin, "Géoroute");
    FC_FeatureAttributeValue georoute = origin
        .getFeatureAttributeValueByName("Géoroute");
    georoute
        .setDefinition("Base de données GEOROUTE® pour la géométrie de l’objet (notamment les points et surfaces d’activité sur les zones couvertes par GEOROUTE®).");
    sProduit.createFeatureAttributeValue(origin, "Fichier");
    FC_FeatureAttributeValue fichier = origin
        .getFeatureAttributeValueByName("Fichier");
    fichier
        .setDefinition("Fichier numérique obtenu auprès d’un prestataire extérieur à l’IGN");
    sProduit.createFeatureAttributeValue(origin, "Plan");
    FC_FeatureAttributeValue plan = origin
        .getFeatureAttributeValueByName("Plan");
    plan.setDefinition("Plan qui a été reporté ou documentation aidant à la localisation.");
    sProduit.createFeatureAttributeValue(origin, "BDParcellaire");
    FC_FeatureAttributeValue bdparcel = origin
        .getFeatureAttributeValueByName("BDParcellaire");
    bdparcel.setDefinition("Base de données BD PARCELLAIRE®.");
    sProduit.createFeatureAttributeValue(origin, "Terrain");
    FC_FeatureAttributeValue terrain = origin
        .getFeatureAttributeValueByName("Terrain");
    terrain.setDefinition("Information provenant d’un passage sur le terrain");
    sProduit.createFeatureAttributeValue(origin, "NR");
    FC_FeatureAttributeValue nr10 = origin.getFeatureAttributeValueByName("NR");
    nr10.setDefinition("Non renseigné");

    // Attribut NOM
    sProduit.createFeatureAttribute(topoCom, "NOM", "string", false);
    AttributeType nom = topoCom.getFeatureAttributeByName("NOM");
    nom.setDefinition("Orthographe du toponyme validée par le bureau de Toponymie.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(topoCom, "IMPORTANCE", "string", true);
    AttributeType importance = topoCom.getFeatureAttributeByName("IMPORTANCE");
    importance.setDefinition("Importance du toponyme dans son environnement.");
    sProduit.createFeatureAttributeValue(importance, "1");
    FC_FeatureAttributeValue un10 = importance
        .getFeatureAttributeValueByName("1");
    un10.setDefinition("Le toponyme de valeur 1 est plus important que le toponyme de valeur 2, lui-même plus important que le toponyme de valeur 3, etc.");
    sProduit.createFeatureAttributeValue(importance, "2");
    FC_FeatureAttributeValue deux10 = importance
        .getFeatureAttributeValueByName("2");
    deux10
        .setDefinition("Le toponyme de valeur 1 est plus important que le toponyme de valeur 2, lui-même plus important que le toponyme de valeur 3, etc.");
    sProduit.createFeatureAttributeValue(importance, "3");
    FC_FeatureAttributeValue trois10 = importance
        .getFeatureAttributeValueByName("3");
    trois10
        .setDefinition("Le toponyme de valeur 1 est plus important que le toponyme de valeur 2, lui-même plus important que le toponyme de valeur 3, etc.");
    sProduit.createFeatureAttributeValue(importance, "4");
    FC_FeatureAttributeValue quatre10 = importance
        .getFeatureAttributeValueByName("4");
    quatre10
        .setDefinition("Le toponyme de valeur 1 est plus important que le toponyme de valeur 2, lui-même plus important que le toponyme de valeur 3, etc.");
    sProduit.createFeatureAttributeValue(importance, "5");
    FC_FeatureAttributeValue cinq10 = importance
        .getFeatureAttributeValueByName("5");
    cinq10
        .setDefinition("Le toponyme de valeur 1 est plus important que le toponyme de valeur 2, lui-même plus important que le toponyme de valeur 3, etc.");
    sProduit.createFeatureAttributeValue(importance, "6");
    FC_FeatureAttributeValue six10 = importance
        .getFeatureAttributeValueByName("6");
    six10
        .setDefinition("Le toponyme de valeur 1 est plus important que le toponyme de valeur 2, lui-même plus important que le toponyme de valeur 3, etc.");
    sProduit.createFeatureAttributeValue(importance, "7");
    FC_FeatureAttributeValue sept10 = importance
        .getFeatureAttributeValueByName("7");
    sept10
        .setDefinition("Le toponyme de valeur 1 est plus important que le toponyme de valeur 2, lui-même plus important que le toponyme de valeur 3, etc.");
    sProduit.createFeatureAttributeValue(importance, "8");
    FC_FeatureAttributeValue huit10 = importance
        .getFeatureAttributeValueByName("8");
    huit10
        .setDefinition("Le toponyme de valeur 1 est plus important que le toponyme de valeur 2, lui-même plus important que le toponyme de valeur 3, etc.");
    sProduit.createFeatureAttributeValue(importance, "NC");
    FC_FeatureAttributeValue ncter10 = importance
        .getFeatureAttributeValueByName("NC");
    ncter10.setDefinition("Non communiqué.");
    sProduit.createFeatureAttributeValue(importance, "NR");
    FC_FeatureAttributeValue nrter10 = importance
        .getFeatureAttributeValueByName("NR");
    nrter10.setDefinition("Non renseigné.");

    // Attribut NATURE
    sProduit.createFeatureAttribute(topoCom, "NATURE", "string", true);
    AttributeType nature10 = topoCom.getFeatureAttributeByName("NATURE");
    nature10
        .setDefinition("Attribut donnant plus précisément la nature de l'objet nommé.");
    sProduit.createFeatureAttributeValue(nature10, "Aire de repos");
    sProduit.createFeatureAttributeValue(nature10, "Aire de service");
    sProduit.createFeatureAttributeValue(nature10, "Carrefour");
    sProduit.createFeatureAttributeValue(nature10, "Chemin");
    sProduit.createFeatureAttributeValue(nature10, "Echangeur");
    sProduit.createFeatureAttributeValue(nature10, "Infrastructure routière");
    sProduit.createFeatureAttributeValue(nature10, "Péage");
    sProduit.createFeatureAttributeValue(nature10, "Parking");
    sProduit.createFeatureAttributeValue(nature10, "Pont");
    sProduit.createFeatureAttributeValue(nature10, "Port");
    sProduit.createFeatureAttributeValue(nature10, "Rond-Point");
    sProduit.createFeatureAttributeValue(nature10, "Tunnel");

    /***************************************************************************
     * Ajout du thème Bâti
     * *************************************************************************/

    // Classe
    // Bati_Indifferencie///////////////////////////////////////////////////

    sProduit.createFeatureType("BATI_INDIFFERENCIE");
    FeatureType batiIndif = (FeatureType) (sProduit
        .getFeatureTypeByName("BATI_INDIFFERENCIE"));
    batiIndif
        .setDefinition("Bâtiment de plus de 20 m2, ne possédant pas de fonction particulière pouvant être décrit dans les autres classes de bâtiments surfaciques : bâtiments d’habitation, d’enseignement...");
    batiIndif.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(batiIndif, "ID", "string", false);
    AttributeType id9 = batiIndif.getFeatureAttributeByName("ID");
    id9.setDefinition("Identifiant du bâtiment.Cet identifiant est unique. Il est stable d’une édition à l’autre. ");

    // Attribut PREC_PLANI
    sProduit.createFeatureAttribute(batiIndif, "PREC_PLANI", "float", true);
    AttributeType prec_plani8 = batiIndif
        .getFeatureAttributeByName("PREC_PLANI");
    prec_plani8
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani8, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani8, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani8, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani8, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani8, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani8, "30.0");

    // Attribut PREC_ALTI
    sProduit.createFeatureAttribute(batiIndif, "PREC_ALTI", "float", true);
    AttributeType prec_alti8 = batiIndif.getFeatureAttributeByName("PREC_ALTI");
    prec_alti8
        .setDefinition("Précision géométrique altimétrique. Attribut précisant la précision géométrique en altimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_alti8, "1.0");
    sProduit.createFeatureAttributeValue(prec_alti8, "2.5");
    sProduit.createFeatureAttributeValue(prec_alti8, "20.0");
    sProduit.createFeatureAttributeValue(prec_alti8, "9999.0");

    // Attribut ORIGIN_BAT
    sProduit.createFeatureAttribute(batiIndif, "ORIGINE_BAT", "string", true);
    AttributeType originBat = batiIndif
        .getFeatureAttributeByName("ORIGINE_BAT");
    originBat
        .setDefinition("Attribut précisant d’où est issu le bâtiment, de quelle base, plan ou levé.");
    sProduit.createFeatureAttributeValue(originBat, "BDTopo");
    sProduit.createFeatureAttributeValue(originBat, "Cadastre");
    sProduit.createFeatureAttributeValue(originBat, "Terrain");
    sProduit.createFeatureAttributeValue(originBat, "Autre");
    sProduit.createFeatureAttributeValue(originBat, "NR");

    // Attribut HAUTEUR
    sProduit.createFeatureAttribute(batiIndif, "HAUTEUR", "integer", false);
    AttributeType hauteur1 = batiIndif.getFeatureAttributeByName("HAUTEUR");
    hauteur1
        .setDefinition("Hauteur du bâtiment correspondant à la différence entre le z le plus élevé du pourtour du bâtiment et un point situé au pied du bâtiment. La hauteur est arrondie au mètre.");

    // Attribut Z_MIN
    sProduit.createFeatureAttribute(batiIndif, "Z_MIN", "float", false);
    AttributeType zMin1 = batiIndif.getFeatureAttributeByName("Z_MIN");
    zMin1
        .setDefinition("Altitude minimale d’un objet linéaire ou surfacique. Cette information est issue de la saisie photogrammétrique et concerne des objets dont les points initiaux et finaux ne sont pas caractéristiques de l’altitude de l’objet (objets surfaciques, objets linéaires isolés). Pour des objets surfaciques comme les bâtiments ou les réservoirs, il s’agit de l’altitude minimum du pourtour tel qu’il est défini en planimétrie (généralement l’altitude aux gouttières) et non de l’altitude minimum absolue. Dans le cas d’un bâtiment obtenu après intégration du bâti issu du cadastre, Z_MIN correspond à une altitude toit médiane calculée, en prenant en compte les altitudes des contours des bâtiments directement contigus s’ils existent. Dans ce cas Z_MAX et Z_MIN prennent la même valeur.");

    // Attribut Z_MAX
    sProduit.createFeatureAttribute(batiIndif, "Z_MAX", "float", false);
    AttributeType zMax1 = batiIndif.getFeatureAttributeByName("Z_MAX");
    zMax1
        .setDefinition("Altitude maximale d’un objet linéaire ou surfacique. Cette information est issue de la saisie photogrammétrique et concerne des objets dont les points initiaux et finaux ne sont pas caractéristiques de l’altitude de l’objet (objets surfaciques, objets linéaires isolés). Pour des objets surfaciques comme les bâtiments ou les réservoirs, il s’agit de l’altitude maximum du pourtour tel qu’il est défini en planimétrie (généralement l’altitude aux gouttières) et non de l’altitude maximum absolue (voir schéma ci-dessus). Dans le cas d’un bâtiment obtenu après intégration du bâti issu du cadastre, Z_MAX correspond à une altitude toit médiane calculée, en prenant en compte les altitudes des contours des bâtiments directement contigus s’ils existent. Dans ce cas Z_MAX et Z_MIN prennent la même valeur.");

    // Classe
    // Bati_Remarquable///////////////////////////////////////////////////

    sProduit.createFeatureType("BATI_REMARQUABLE");
    FeatureType batiRemarquable = (FeatureType) (sProduit
        .getFeatureTypeByName("BATI_REMARQUABLE"));
    batiRemarquable
        .setDefinition("Bâtiment de plus de 20 m2 possédant une fonction, contrairement aux bâtiments indifférenciés, et dont la fonction est autre qu’industrielle (ces derniers sont regroupés dans la classe BATI_INDUSTRIEL). Il s’agit des bâtiments administratifs, religieux, sportifs, et relatifs au transport.");
    batiRemarquable.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(batiRemarquable, "ID", "string", false);
    AttributeType id10 = batiRemarquable.getFeatureAttributeByName("ID");
    id10.setDefinition("Identifiant du bâtiment.Cet identifiant est unique. Il est stable d’une édition à l’autre. ");

    // Attribut PREC_PLANI
    sProduit.createFeatureAttribute(batiRemarquable, "PREC_PLANI", "float",
        true);
    AttributeType prec_plani9 = batiRemarquable
        .getFeatureAttributeByName("PREC_PLANI");
    prec_plani9
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani9, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani9, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani9, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani9, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani9, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani9, "30.0");

    // Attribut PREC_ALTI
    sProduit
        .createFeatureAttribute(batiRemarquable, "PREC_ALTI", "float", true);
    AttributeType prec_alti9 = batiRemarquable
        .getFeatureAttributeByName("PREC_ALTI");
    prec_alti9
        .setDefinition("Précision géométrique altimétrique. Attribut précisant la précision géométrique en altimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_alti9, "1.0");
    sProduit.createFeatureAttributeValue(prec_alti9, "2.5");
    sProduit.createFeatureAttributeValue(prec_alti9, "20.0");
    sProduit.createFeatureAttributeValue(prec_alti9, "9999.0");

    // Attribut ORIGIN_BAT
    sProduit.createFeatureAttribute(batiRemarquable, "ORIGINE_BAT", "string",
        true);
    AttributeType originBat1 = batiRemarquable
        .getFeatureAttributeByName("ORIGINE_BAT");
    originBat1
        .setDefinition("Attribut précisant d’où est issu le bâtiment, de quelle base, plan ou levé.");
    sProduit.createFeatureAttributeValue(originBat1, "BDTopo");
    sProduit.createFeatureAttributeValue(originBat1, "Cadastre");
    sProduit.createFeatureAttributeValue(originBat1, "Terrain");
    sProduit.createFeatureAttributeValue(originBat1, "Autre");
    sProduit.createFeatureAttributeValue(originBat1, "NR");

    // Attribut NATURE
    sProduit.createFeatureAttribute(batiRemarquable, "NATURE", "string", true);
    AttributeType nature15 = batiRemarquable
        .getFeatureAttributeByName("NATURE");
    nature15
        .setDefinition("Attribut permettant de distinguer différents types de bâtiments.");
    sProduit.createFeatureAttributeValue(nature15, "Aérogare");
    FC_FeatureAttributeValue aerogare = nature15
        .getFeatureAttributeValueByName("Aérogare");
    aerogare
        .setDefinition("Ensemble des bâtiments d'un aéroport réservés aux voyageurs et aux marchandises.");
    sProduit.createFeatureAttributeValue(nature15, "Arc de triomphe");
    FC_FeatureAttributeValue arc = nature15
        .getFeatureAttributeValueByName("Arc de triomphe");
    arc.setDefinition("Portique monumental : arc de triomphe, porte de ville.");
    sProduit.createFeatureAttributeValue(nature15, "Arène ou théâtre antique");
    FC_FeatureAttributeValue arene = nature15
        .getFeatureAttributeValueByName("Arène ou théâtre antique");
    arene
        .setDefinition("Vaste édifice à gradins, de forme en partie ou totalement ronde ou elliptique : amphithéâtre, arène, théâtre antique, théâtre de plein air.");
    sProduit.createFeatureAttributeValue(nature15, "Bâtiment religieux divers");
    FC_FeatureAttributeValue batRel = nature15
        .getFeatureAttributeValueByName("Bâtiment religieux divers");
    batRel
        .setDefinition("Bâtiment réservé à l’exercice d’un culte religieux, autre qu’une chapelle ou qu’une église (voir ces valeurs) : mosquée, synagogue, temple.");
    sProduit.createFeatureAttributeValue(nature15, "Bâtiment sportif");
    FC_FeatureAttributeValue batSportif = nature15
        .getFeatureAttributeValueByName("Bâtiment sportif");
    batSportif
        .setDefinition("Bâtiment réservé à la pratique sportive : gymnase, piscine couverte, salle de sport, tennis couvert.");
    sProduit.createFeatureAttributeValue(nature15, "Chapelle");
    FC_FeatureAttributeValue chapelle = nature15
        .getFeatureAttributeValueByName("Chapelle");
    chapelle
        .setDefinition("Petit édifice religieux catholique de forme caractéristique");
    sProduit.createFeatureAttributeValue(nature15, "Château");
    FC_FeatureAttributeValue chateau = nature15
        .getFeatureAttributeValueByName("Château");
    chateau
        .setDefinition("Habitation ou ancienne habitation féodale, royale ou seigneuriale : château, château fort, citadelle");
    sProduit.createFeatureAttributeValue(nature15, "Eglise");
    FC_FeatureAttributeValue eglise = nature15
        .getFeatureAttributeValueByName("Eglise");
    eglise
        .setDefinition("Edifice religieux catholique de forme caractéristique : basilique, cathédrale, église.");
    sProduit.createFeatureAttributeValue(nature15, "Fort, blockhaus, casemate");
    FC_FeatureAttributeValue fort = nature15
        .getFeatureAttributeValueByName("Fort, blockhaus, casemate");
    fort.setDefinition("Ouvrage militaire : blockhaus, casemate, fort, ouvrage fortifié.");
    sProduit.createFeatureAttributeValue(nature15, "Gare");
    FC_FeatureAttributeValue gare = nature15
        .getFeatureAttributeValueByName("Gare");
    gare.setDefinition("Bâtiment servant à l'embarquement et au débarquement des voyageurs en train.");
    sProduit.createFeatureAttributeValue(nature15, "Mairie");
    FC_FeatureAttributeValue mairie = nature15
        .getFeatureAttributeValueByName("Mairie");
    mairie
        .setDefinition("Edifice où se trouvent les services de l'administration municipale, appelé aussi hôtel de ville.");
    sProduit.createFeatureAttributeValue(nature15, "Monument");
    FC_FeatureAttributeValue monument = nature15
        .getFeatureAttributeValueByName("Monument");
    monument
        .setDefinition("Monument commémoratif quelconque, à l’exception des arcs de triomphe (voir cette valeur d’attribut).");
    sProduit.createFeatureAttributeValue(nature15, "Péage");
    FC_FeatureAttributeValue peage1 = nature15
        .getFeatureAttributeValueByName("Péage");
    peage1.setDefinition("Bâtiment où sont perçus les droits d'usage.");
    sProduit.createFeatureAttributeValue(nature15, "Préfecture");
    FC_FeatureAttributeValue prefecture = nature15
        .getFeatureAttributeValueByName("Préfecture");
    prefecture
        .setDefinition("Bâtiment où sont installés les services préfectoraux.");
    sProduit.createFeatureAttributeValue(nature15, "Sous-préfecture");
    FC_FeatureAttributeValue sousPrefecture = nature15
        .getFeatureAttributeValueByName("Sous-préfecture");
    sousPrefecture
        .setDefinition("Bâtiment où sont les bureaux du sous-préfet : chef lieu d’arrondissement.");
    sProduit.createFeatureAttributeValue(nature15, "Tour, donjon, moulin");
    FC_FeatureAttributeValue tour = nature15
        .getFeatureAttributeValueByName("Tour, donjon, moulin");
    tour.setDefinition("Bâtiment remarquable dans le Paysage par sa forme élevée : donjon, moulin à vent, tour, tour de contrôle.");
    sProduit.createFeatureAttributeValue(nature15, "Tribune");
    FC_FeatureAttributeValue tribune = nature15
        .getFeatureAttributeValueByName("Tribune");
    tribune
        .setDefinition("Tribune de terrain de sport (stade, hippodrome, vélodrome,...).");

    // Attribut HAUTEUR
    sProduit.createFeatureAttribute(batiRemarquable, "HAUTEUR", "integer",
        false);
    AttributeType hauteur2 = batiRemarquable
        .getFeatureAttributeByName("HAUTEUR");
    hauteur2
        .setDefinition("Hauteur du bâtiment correspondant à la différence entre le z le plus élevé du pourtour du bâtiment et un point situé au pied du bâtiment. La hauteur est arrondie au mètre.");

    // Attribut Z_MIN
    sProduit.createFeatureAttribute(batiRemarquable, "Z_MIN", "float", false);
    AttributeType zMin2 = batiRemarquable.getFeatureAttributeByName("Z_MIN");
    zMin2
        .setDefinition("Altitude minimale d’un objet linéaire ou surfacique. Cette information est issue de la saisie photogrammétrique et concerne des objets dont les points initiaux et finaux ne sont pas caractéristiques de l’altitude de l’objet (objets surfaciques, objets linéaires isolés). Pour des objets surfaciques comme les bâtiments ou les réservoirs, il s’agit de l’altitude minimum du pourtour tel qu’il est défini en planimétrie (généralement l’altitude aux gouttières) et non de l’altitude minimum absolue. Dans le cas d’un bâtiment obtenu après intégration du bâti issu du cadastre, Z_MIN correspond à une altitude toit médiane calculée, en prenant en compte les altitudes des contours des bâtiments directement contigus s’ils existent. Dans ce cas Z_MAX et Z_MIN prennent la même valeur.");

    // Attribut Z_MAX
    sProduit.createFeatureAttribute(batiRemarquable, "Z_MAX", "float", false);
    AttributeType zMax2 = batiRemarquable.getFeatureAttributeByName("Z_MAX");
    zMax2
        .setDefinition("Altitude maximale d’un objet linéaire ou surfacique. Cette information est issue de la saisie photogrammétrique et concerne des objets dont les points initiaux et finaux ne sont pas caractéristiques de l’altitude de l’objet (objets surfaciques, objets linéaires isolés). Pour des objets surfaciques comme les bâtiments ou les réservoirs, il s’agit de l’altitude maximum du pourtour tel qu’il est défini en planimétrie (généralement l’altitude aux gouttières) et non de l’altitude maximum absolue (voir schéma ci-dessus). Dans le cas d’un bâtiment obtenu après intégration du bâti issu du cadastre, Z_MAX correspond à une altitude toit médiane calculée, en prenant en compte les altitudes des contours des bâtiments directement contigus s’ils existent. Dans ce cas Z_MAX et Z_MIN prennent la même valeur.");

    // Classe
    // Bati_Industriel///////////////////////////////////////////////////

    sProduit.createFeatureType("BATI_INDUSTRIEL");
    FeatureType batiIndus = (FeatureType) (sProduit
        .getFeatureTypeByName("BATI_INDUSTRIEL"));
    batiIndus
        .setDefinition("Bâtiment de plus de 20 m2 à caractère industriel, commercial ou agricole.");
    batiIndus.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(batiIndus, "ID", "string", false);
    AttributeType id11 = batiIndus.getFeatureAttributeByName("ID");
    id11.setDefinition("Identifiant du bâtiment.Cet identifiant est unique. Il est stable d’une édition à l’autre.");

    // Attribut PREC_PLANI
    sProduit.createFeatureAttribute(batiIndus, "PREC_PLANI", "float", true);
    AttributeType prec_plani10 = batiIndus
        .getFeatureAttributeByName("PREC_PLANI");
    prec_plani10
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani10, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani10, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani10, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani10, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani10, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani10, "30.0");

    // Attribut PREC_ALTI
    sProduit.createFeatureAttribute(batiIndus, "PREC_ALTI", "float", true);
    AttributeType prec_alti10 = batiIndus
        .getFeatureAttributeByName("PREC_ALTI");
    prec_alti10
        .setDefinition("Précision géométrique altimétrique. Attribut précisant la précision géométrique en altimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_alti10, "1.0");
    sProduit.createFeatureAttributeValue(prec_alti10, "2.5");
    sProduit.createFeatureAttributeValue(prec_alti10, "20.0");
    sProduit.createFeatureAttributeValue(prec_alti10, "9999.0");

    // Attribut ORIGIN_BAT
    sProduit.createFeatureAttribute(batiIndus, "ORIGINE_BAT", "string", true);
    AttributeType originBat2 = batiIndus
        .getFeatureAttributeByName("ORIGINE_BAT");
    originBat2
        .setDefinition("Attribut précisant d’où est issu le bâtiment, de quelle base, plan ou levé.");
    sProduit.createFeatureAttributeValue(originBat2, "BDTopo");
    sProduit.createFeatureAttributeValue(originBat2, "Cadastre");
    sProduit.createFeatureAttributeValue(originBat2, "Terrain");
    sProduit.createFeatureAttributeValue(originBat2, "Autre");
    sProduit.createFeatureAttributeValue(originBat2, "NR");

    // Attribut NATURE
    sProduit.createFeatureAttribute(batiIndus, "NATURE", "string", true);
    AttributeType nature16 = batiIndus.getFeatureAttributeByName("NATURE");
    nature16
        .setDefinition("Attribut permettant de distinguer différents types de bâtiments à caractère industriel, commercial ou agricole.");
    sProduit.createFeatureAttributeValue(nature16, "Bâtiment agricole");
    FC_FeatureAttributeValue batAgri = nature16
        .getFeatureAttributeValueByName("Bâtiment agricole");
    batAgri
        .setDefinition("Bâtiment réservé à des activités agricoles : bâtiment d'élevage industriel, hangar agricole (grand), minoterie.");
    sProduit.createFeatureAttributeValue(nature16, "Bâtiment commercial");
    FC_FeatureAttributeValue batCom = nature16
        .getFeatureAttributeValueByName("Bâtiment commercial");
    batCom
        .setDefinition("Bâtiment de grande surface réservé à des activités commerciales : centre commercial, hypermarché, magasin (grand, isolé), parc des expositions (bâtiment).");
    sProduit.createFeatureAttributeValue(nature16, "Bâtiment industriel");
    FC_FeatureAttributeValue batIndustriel = nature16
        .getFeatureAttributeValueByName("Bâtiment industriel");
    batIndustriel
        .setDefinition("Bâtiment réservé à des activités industrielles : abattoir, atelier (grand), auvent de quai de gare, bâtiment industriel (grand), centrale électrique (bâtiment), construction technique, entrepôt, hangar industriel (grand), scierie, usine.");
    sProduit.createFeatureAttributeValue(nature16, "Serre");
    FC_FeatureAttributeValue serre = nature16
        .getFeatureAttributeValueByName("Serre");
    serre
        .setDefinition("Abri clos à parois translucides destiné à protéger les végétaux du froid : jardinerie, serre. Les serres en arceaux de moins de 20 m de long sont exclues. Les serres situées à moins de 3 m les unes des autres sont modélisées par un seul objet englobant l’ensemble des serres en s’appuyant au maximum sur leurs contours.");
    sProduit.createFeatureAttributeValue(nature16, "Silo");
    FC_FeatureAttributeValue silo = nature16
        .getFeatureAttributeValueByName("Silo");
    silo.setDefinition("Réservoir, qui chargé par le haut se vide par le bas, et qui sert de dépôt, de magasin, etc. Le silo est exclusivement destiné aux produits agricoles : cuve à vin, silo");

    // Attribut HAUTEUR
    sProduit.createFeatureAttribute(batiIndus, "HAUTEUR", "integer", false);
    AttributeType hauteur3 = batiIndus.getFeatureAttributeByName("HAUTEUR");
    hauteur3
        .setDefinition("Hauteur du bâtiment correspondant à la différence entre le z le plus élevé du pourtour du bâtiment et un point situé au pied du bâtiment. La hauteur est arrondie au mètre.");

    // Attribut Z_MIN
    sProduit.createFeatureAttribute(batiIndus, "Z_MIN", "float", false);
    AttributeType zMin3 = batiIndus.getFeatureAttributeByName("Z_MIN");
    zMin3
        .setDefinition("Altitude minimale d’un objet linéaire ou surfacique. Cette information est issue de la saisie photogrammétrique et concerne des objets dont les points initiaux et finaux ne sont pas caractéristiques de l’altitude de l’objet (objets surfaciques, objets linéaires isolés). Pour des objets surfaciques comme les bâtiments ou les réservoirs, il s’agit de l’altitude minimum du pourtour tel qu’il est défini en planimétrie (généralement l’altitude aux gouttières) et non de l’altitude minimum absolue. Dans le cas d’un bâtiment obtenu après intégration du bâti issu du cadastre, Z_MIN correspond à une altitude toit médiane calculée, en prenant en compte les altitudes des contours des bâtiments directement contigus s’ils existent. Dans ce cas Z_MAX et Z_MIN prennent la même valeur.");

    // Attribut Z_MAX
    sProduit.createFeatureAttribute(batiIndus, "Z_MAX", "float", false);
    AttributeType zMax3 = batiIndus.getFeatureAttributeByName("Z_MAX");
    zMax3
        .setDefinition("Altitude maximale d’un objet linéaire ou surfacique. Cette information est issue de la saisie photogrammétrique et concerne des objets dont les points initiaux et finaux ne sont pas caractéristiques de l’altitude de l’objet (objets surfaciques, objets linéaires isolés). Pour des objets surfaciques comme les bâtiments ou les réservoirs, il s’agit de l’altitude maximum du pourtour tel qu’il est défini en planimétrie (généralement l’altitude aux gouttières) et non de l’altitude maximum absolue (voir schéma ci-dessus). Dans le cas d’un bâtiment obtenu après intégration du bâti issu du cadastre, Z_MAX correspond à une altitude toit médiane calculée, en prenant en compte les altitudes des contours des bâtiments directement contigus s’ils existent. Dans ce cas Z_MAX et Z_MIN prennent la même valeur.");

    // Classe
    // Construction_Légère///////////////////////////////////////////////////

    sProduit.createFeatureType("CONSTRUCTION_LEGERE");
    FeatureType constructionLeg = (FeatureType) (sProduit
        .getFeatureTypeByName("CONSTRUCTION_LEGERE"));
    constructionLeg
        .setDefinition("Structure légère non attachée au sol par l’intermédiaire de fondations ou bâtiment quelconque ouvert sur au moins un côté.");
    constructionLeg.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(constructionLeg, "ID", "string", false);
    AttributeType id12 = constructionLeg.getFeatureAttributeByName("ID");
    id12.setDefinition("Identifiant du bâtiment.Cet identifiant est unique. Il est stable d’une édition à l’autre. ");

    // Attribut PREC_PLANI
    sProduit.createFeatureAttribute(constructionLeg, "PREC_PLANI", "float",
        true);
    AttributeType prec_plani11 = constructionLeg
        .getFeatureAttributeByName("PREC_PLANI");
    prec_plani11
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani11, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani11, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani11, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani11, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani11, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani11, "30.0");

    // Attribut PREC_ALTI
    sProduit
        .createFeatureAttribute(constructionLeg, "PREC_ALTI", "float", true);
    AttributeType prec_alti11 = constructionLeg
        .getFeatureAttributeByName("PREC_ALTI");
    prec_alti11
        .setDefinition("Précision géométrique altimétrique. Attribut précisant la précision géométrique en altimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_alti11, "1.0");
    sProduit.createFeatureAttributeValue(prec_alti11, "2.5");
    sProduit.createFeatureAttributeValue(prec_alti11, "20.0");
    sProduit.createFeatureAttributeValue(prec_alti11, "9999.0");

    // Attribut ORIGIN_BAT
    sProduit.createFeatureAttribute(constructionLeg, "ORIGINE_BAT", "string",
        true);
    AttributeType originBat3 = constructionLeg
        .getFeatureAttributeByName("ORIGINE_BAT");
    originBat3
        .setDefinition("Attribut précisant d’où est issu le bâtiment, de quelle base, plan ou levé.");
    sProduit.createFeatureAttributeValue(originBat3, "BDTopo");
    sProduit.createFeatureAttributeValue(originBat3, "Cadastre");
    sProduit.createFeatureAttributeValue(originBat3, "Terrain");
    sProduit.createFeatureAttributeValue(originBat3, "Autre");
    sProduit.createFeatureAttributeValue(originBat3, "NR");

    // Attribut HAUTEUR
    sProduit.createFeatureAttribute(constructionLeg, "HAUTEUR", "integer",
        false);
    AttributeType hauteur4 = constructionLeg
        .getFeatureAttributeByName("HAUTEUR");
    hauteur4
        .setDefinition("Hauteur du bâtiment correspondant à la différence entre le z le plus élevé du pourtour du bâtiment et un point situé au pied du bâtiment. La hauteur est arrondie au mètre.");

    // Classe Cimetiere///////////////////////////////////////////////////

    sProduit.createFeatureType("CIMETIERE");
    FeatureType cimetiere = (FeatureType) (sProduit
        .getFeatureTypeByName("CIMETIERE"));
    cimetiere
        .setDefinition("Lieu où l’on enterre les morts. Cimetière communal, islamique, israélite, ou militaire.");
    cimetiere.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(cimetiere, "ID", "string", false);
    AttributeType id13 = cimetiere.getFeatureAttributeByName("ID");
    id13.setDefinition("Identifiant du bâtiment.Cet identifiant est unique. Il est stable d’une édition à l’autre. ");

    // Attribut PREC_PLANI
    sProduit.createFeatureAttribute(cimetiere, "PREC_PLANI", "float", true);
    AttributeType prec_plani12 = cimetiere
        .getFeatureAttributeByName("PREC_PLANI");
    prec_plani12
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani12, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani12, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani12, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani12, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani12, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani12, "30.0");

    // Attribut PREC_ALTI
    sProduit.createFeatureAttribute(cimetiere, "PREC_ALTI", "float", true);
    AttributeType prec_alti12 = cimetiere
        .getFeatureAttributeByName("PREC_ALTI");
    prec_alti12
        .setDefinition("Précision géométrique altimétrique. Attribut précisant la précision géométrique en altimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_alti12, "1.0");
    sProduit.createFeatureAttributeValue(prec_alti12, "2.5");
    sProduit.createFeatureAttributeValue(prec_alti12, "20.0");
    sProduit.createFeatureAttributeValue(prec_alti12, "9999.0");

    // Attribut NATURE
    sProduit.createFeatureAttribute(cimetiere, "NATURE", "string", true);
    AttributeType nature17 = cimetiere.getFeatureAttributeByName("NATURE");
    nature17
        .setDefinition("Attribut permettant de distinguer un objet géographique à vocation militaire, ou simplement géré par le ministère de la défense, d’un objet civil. La valeur « Militaire » est également affectée aux cimetières militaires gérés par le Ministère des Anciens Combattants ou par des états étrangers.");
    sProduit.createFeatureAttributeValue(nature17, "Militaire");
    sProduit.createFeatureAttributeValue(nature17, "Autre");

    // Classe
    // Piste_Aérodrome///////////////////////////////////////////////////

    sProduit.createFeatureType("PISTE_AERODROME");
    FeatureType pisteAero = (FeatureType) (sProduit
        .getFeatureTypeByName("PISTE_AERODROME"));
    pisteAero
        .setDefinition("Aire située sur un aérodrome, aménagée afin de servir au roulement des aéronefs, au décollage et à l’atterrissage, en dur ou en herbe.");
    pisteAero.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(pisteAero, "ID", "string", false);
    AttributeType id14 = pisteAero.getFeatureAttributeByName("ID");
    id14.setDefinition("Identifiant de la piste d'aérodrome.Cet identifiant est unique. Il est stable d’une édition à l’autre. ");

    // Attribut PREC_PLANI
    sProduit.createFeatureAttribute(pisteAero, "PREC_PLANI", "float", true);
    AttributeType prec_plani13 = pisteAero
        .getFeatureAttributeByName("PREC_PLANI");
    prec_plani13
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani13, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani13, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani13, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani13, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani13, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani13, "30.0");

    // Attribut PREC_ALTI
    sProduit.createFeatureAttribute(pisteAero, "PREC_ALTI", "float", true);
    AttributeType prec_alti13 = pisteAero
        .getFeatureAttributeByName("PREC_ALTI");
    prec_alti13
        .setDefinition("Précision géométrique altimétrique. Attribut précisant la précision géométrique en altimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_alti13, "1.0");
    sProduit.createFeatureAttributeValue(prec_alti13, "2.5");
    sProduit.createFeatureAttributeValue(prec_alti13, "20.0");
    sProduit.createFeatureAttributeValue(prec_alti13, "9999.0");

    // Attribut NATURE
    sProduit.createFeatureAttribute(pisteAero, "NATURE", "string", true);
    AttributeType nature18 = pisteAero.getFeatureAttributeByName("NATURE");
    nature18
        .setDefinition("Attribut permettant de distinguer différentes natures d'aérodrome.");
    sProduit.createFeatureAttributeValue(nature18, "Piste en dur");
    sProduit.createFeatureAttributeValue(nature18, "Piste en herbe");

    // Attribut Z_MOYEN
    sProduit.createFeatureAttribute(pisteAero, "Z_MOYEN", "float", false);
    AttributeType z_moy2 = pisteAero.getFeatureAttributeByName("Z_MOYEN");
    z_moy2
        .setDefinition("Altitude moyenne des points composants la géométrie de l’objet telle qu’il a été saisi à l’origine lorsqu’il est issu d’une saisie photogrammétrique.");

    // Classe Réservoir///////////////////////////////////////////////////

    sProduit.createFeatureType("RESERVOIR");
    FeatureType reservoir = (FeatureType) (sProduit
        .getFeatureTypeByName("RESERVOIR"));
    reservoir
        .setDefinition("Réservoir (eau, matières industrielles,…) de plus de 10m de diamètre.");
    reservoir.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(reservoir, "ID", "string", false);
    AttributeType id15 = reservoir.getFeatureAttributeByName("ID");
    id15.setDefinition("Identifiant du réservoir.Cet identifiant est unique. Il est stable d’une édition à l’autre. ");

    // Attribut PREC_PLANI
    sProduit.createFeatureAttribute(reservoir, "PREC_PLANI", "float", true);
    AttributeType prec_plani14 = reservoir
        .getFeatureAttributeByName("PREC_PLANI");
    prec_plani14
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani14, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani14, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani14, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani14, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani14, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani14, "30.0");

    // Attribut PREC_ALTI
    sProduit.createFeatureAttribute(reservoir, "PREC_ALTI", "float", true);
    AttributeType prec_alti14 = reservoir
        .getFeatureAttributeByName("PREC_ALTI");
    prec_alti14
        .setDefinition("Précision géométrique altimétrique. Attribut précisant la précision géométrique en altimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_alti14, "1.0");
    sProduit.createFeatureAttributeValue(prec_alti14, "2.5");
    sProduit.createFeatureAttributeValue(prec_alti14, "20.0");
    sProduit.createFeatureAttributeValue(prec_alti14, "9999.0");

    // Attribut ORIGIN_BAT
    sProduit.createFeatureAttribute(reservoir, "ORIGINE_BAT", "string", true);
    AttributeType originBat4 = reservoir
        .getFeatureAttributeByName("ORIGINE_BAT");
    originBat4
        .setDefinition("Attribut précisant d’où est issu le bâtiment, de quelle base, plan ou levé.");
    sProduit.createFeatureAttributeValue(originBat4, "BDTopo");
    sProduit.createFeatureAttributeValue(originBat4, "Cadastre");
    sProduit.createFeatureAttributeValue(originBat4, "Terrain");
    sProduit.createFeatureAttributeValue(originBat4, "Autre");
    sProduit.createFeatureAttributeValue(originBat4, "NR");

    // Attribut NATURE
    sProduit.createFeatureAttribute(reservoir, "NATURE", "string", true);
    AttributeType nature19 = reservoir.getFeatureAttributeByName("NATURE");
    nature19
        .setDefinition("Attribut permettant de distinguer différents types de réservoirs.");
    sProduit.createFeatureAttributeValue(nature19, "Château d'eau");
    FC_FeatureAttributeValue chateauEau = nature19
        .getFeatureAttributeValueByName("Château d'eau");
    chateauEau
        .setDefinition("Réservoir d’eau construit en hauteur pour surélever le niveau de l’eau par rapport aux constructions. Contour extérieur du château d’eau (circonférence maximum), à l’altitude de ce contour (altitude de l’arête supérieure en cas de face verticale).");
    sProduit.createFeatureAttributeValue(nature19, "Réservoir d'eau");
    FC_FeatureAttributeValue reservoirEau = nature19
        .getFeatureAttributeValueByName("Réservoir d'eau");
    reservoirEau
        .setDefinition("Réservoirs d’eau et châteaux d’eau au sol dans lesquels la réserve d’eau est située au niveau du sol. Le réservoir est souvent semi enterré. Contour extérieur du réservoir tel qu’il apparaît vu d’avion.");
    sProduit.createFeatureAttributeValue(nature19, "Réservoir industriel");
    FC_FeatureAttributeValue reservoirIndus = nature19
        .getFeatureAttributeValueByName("Réservoir industriel");
    reservoirIndus
        .setDefinition("Réservoir de matière première industrielle : gazomètre, réservoir d’hydrocarbure, réservoir de matériaux de construction, réservoir industriel. Tous les réservoirs de plus de 10 m de large sont inclus. Contour extérieur du réservoir tel qu’il apparaît vu d’avion.");
    sProduit.createFeatureAttributeValue(nature19, "NR");
    FC_FeatureAttributeValue nr19 = nature19
        .getFeatureAttributeValueByName("NR");
    nr19.setDefinition("L'information n'est pas présente dans la base");

    // Attribut HAUTEUR
    sProduit.createFeatureAttribute(reservoir, "HAUTEUR", "integer", false);
    AttributeType hauteur5 = reservoir.getFeatureAttributeByName("HAUTEUR");
    hauteur5
        .setDefinition("Hauteur du réservoir correspondant à la différence entre le z le plus élevé du pourtour du réservoir et un point situé au pied. La hauteur est arrondie au mètre.");

    // Attribut Z_MIN
    sProduit.createFeatureAttribute(reservoir, "Z_MIN", "float", false);
    AttributeType zMin4 = reservoir.getFeatureAttributeByName("Z_MIN");
    zMin4
        .setDefinition("Altitude minimale d’un objet linéaire ou surfacique. Cette information est issue de la saisie photogrammétrique et concerne des objets dont les points initiaux et finaux ne sont pas caractéristiques de l’altitude de l’objet (objets surfaciques, objets linéaires isolés). Pour des objets surfaciques comme les bâtiments ou les réservoirs, il s’agit de l’altitude minimum du pourtour tel qu’il est défini en planimétrie (généralement l’altitude aux gouttières) et non de l’altitude minimum absolue.");

    // Attribut Z_MAX
    sProduit.createFeatureAttribute(reservoir, "Z_MAX", "float", false);
    AttributeType zMax4 = reservoir.getFeatureAttributeByName("Z_MAX");
    zMax4
        .setDefinition("Altitude maximale d’un objet linéaire ou surfacique. Cette information est issue de la saisie photogrammétrique et concerne des objets dont les points initiaux et finaux ne sont pas caractéristiques de l’altitude de l’objet (objets surfaciques, objets linéaires isolés). Pour des objets surfaciques comme les bâtiments ou les réservoirs, il s’agit de l’altitude maximum du pourtour tel qu’il est défini en planimétrie (généralement l’altitude aux gouttières) et non de l’altitude maximum absolue (voir schéma ci-dessus).");

    // Classe
    // Terrain_Sport///////////////////////////////////////////////////

    sProduit.createFeatureType("TERRAIN_SPORT");
    FeatureType terrainSport = (FeatureType) (sProduit
        .getFeatureTypeByName("TERRAIN_SPORT"));
    terrainSport.setDefinition("Equipememnt sportif de plein air.");
    terrainSport.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(terrainSport, "ID", "string", false);
    AttributeType id17 = terrainSport.getFeatureAttributeByName("ID");
    id17.setDefinition("Identifiant du terrain de sport.Cet identifiant est unique. Il est stable d’une édition à l’autre. ");

    // Attribut PREC_PLANI
    sProduit.createFeatureAttribute(terrainSport, "PREC_PLANI", "float", true);
    AttributeType prec_plani15 = terrainSport
        .getFeatureAttributeByName("PREC_PLANI");
    prec_plani15
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani15, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani15, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani15, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani15, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani15, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani15, "30.0");

    // Attribut PREC_ALTI
    sProduit.createFeatureAttribute(terrainSport, "PREC_ALTI", "float", true);
    AttributeType prec_alti15 = terrainSport
        .getFeatureAttributeByName("PREC_ALTI");
    prec_alti15
        .setDefinition("Précision géométrique altimétrique. Attribut précisant la précision géométrique en altimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_alti15, "1.0");
    sProduit.createFeatureAttributeValue(prec_alti15, "2.5");
    sProduit.createFeatureAttributeValue(prec_alti15, "20.0");
    sProduit.createFeatureAttributeValue(prec_alti15, "9999.0");

    // Attribut NATURE
    sProduit.createFeatureAttribute(terrainSport, "NATURE", "string", true);
    AttributeType nature20 = terrainSport.getFeatureAttributeByName("NATURE");
    nature20
        .setDefinition("Attribut permettant de distinguer différents types de terrains de sport.");
    sProduit.createFeatureAttributeValue(nature20, "Indifférencié");
    FC_FeatureAttributeValue indif = nature20
        .getFeatureAttributeValueByName("Indifférencié");
    indif
        .setDefinition("Grand terrain découvert servant à la pratique de sports collectifs tels que le football, le rugby, etc. : plate-forme multisports, terrain d’entraînement, terrain de football, terrain de rugby.");
    sProduit.createFeatureAttributeValue(nature20, "Piste de sport");
    FC_FeatureAttributeValue pisteSport = nature20
        .getFeatureAttributeValueByName("Piste de sport");
    pisteSport
        .setDefinition("Large piste réservée à la course : autodrome (piste), circuit auto-moto (piste), cynodrome (piste), hippodrome (piste), vélodrome (piste).");
    sProduit.createFeatureAttributeValue(nature20, "Terrain de tennis");
    FC_FeatureAttributeValue terrainTennis = nature20
        .getFeatureAttributeValueByName("Terrain de tennis");
    terrainTennis
        .setDefinition("Terrain spécialement aménagé pour la pratique du tennis.");
    sProduit.createFeatureAttributeValue(nature20, "Bassin de natation");
    FC_FeatureAttributeValue bassinNat = nature20
        .getFeatureAttributeValueByName("Bassin de natation");
    bassinNat
        .setDefinition("Bassin de natation d’une piscine découverte : bassin de natation, piscine (découverte).");

    // Attribut Z_MOYEN
    sProduit.createFeatureAttribute(terrainSport, "Z_MOYEN", "float", false);
    AttributeType z_moy3 = terrainSport.getFeatureAttributeByName("Z_MOYEN");
    z_moy3
        .setDefinition("Altitude moyenne des points composants la géométrie de l’objet telle qu’il a été saisi à l’origine lorsqu’il est issu d’une saisie photogrammétrique.");

    // Classe
    // Construction_Linéaire///////////////////////////////////////////////////

    sProduit.createFeatureType("CONSTRUCTION_LINEAIRE");
    FeatureType constructLine = (FeatureType) (sProduit
        .getFeatureTypeByName("CONSTRUCTION_LINEAIRE"));
    constructLine
        .setDefinition("Construction dont la forme générale est linéaire. Exemples : barrage, mur anti-bruit, ruines, etc.");
    constructLine.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(constructLine, "ID", "string", false);
    AttributeType id18 = constructLine.getFeatureAttributeByName("ID");
    id18.setDefinition("Identifiant de la construction.Cet identifiant est unique. Il est stable d’une édition à l’autre. ");

    // Attribut PREC_PLANI
    sProduit.createFeatureAttribute(constructLine, "PREC_PLANI", "float", true);
    AttributeType prec_plani16 = constructLine
        .getFeatureAttributeByName("PREC_PLANI");
    prec_plani16
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani16, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani16, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani16, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani16, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani16, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani16, "30.0");

    // Attribut PREC_ALTI
    sProduit.createFeatureAttribute(constructLine, "PREC_ALTI", "float", true);
    AttributeType prec_alti16 = constructLine
        .getFeatureAttributeByName("PREC_ALTI");
    prec_alti16
        .setDefinition("Précision géométrique altimétrique. Attribut précisant la précision géométrique en altimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_alti16, "1.0");
    sProduit.createFeatureAttributeValue(prec_alti16, "2.5");
    sProduit.createFeatureAttributeValue(prec_alti16, "20.0");
    sProduit.createFeatureAttributeValue(prec_alti16, "9999.0");

    // Attribut NATURE
    sProduit.createFeatureAttribute(constructLine, "NATURE", "string", true);
    AttributeType nature21 = constructLine.getFeatureAttributeByName("NATURE");
    nature21
        .setDefinition("Attribut permettant de distinguer différents types d'ouvrage.");
    sProduit.createFeatureAttributeValue(nature21, "Indifférencié");
    FC_FeatureAttributeValue indif2 = nature21
        .getFeatureAttributeValueByName("Indifférencié");
    indif2
        .setDefinition("Mur d’enceinte ou de défense d’un château fort, d’une prison,... Ouvrage de forme générale linéaire non distingué par une autre valeur d'attribut : Fronton de pelote basque, mur de fortification, mur de prison, pare-avalanche, piste de luge, piste de bobsleigh, clôture.");
    sProduit.createFeatureAttributeValue(nature21, "Barrage");
    FC_FeatureAttributeValue barrage = nature21
        .getFeatureAttributeValueByName("Barrage");
    barrage
        .setDefinition("Ouvrage établi en travers du lit d’un cours d’eau et créant une dénivellation du plan d’eau entre l’amont et l’aval : barrage, vanne.");
    sProduit.createFeatureAttributeValue(nature21, "Mur anti-bruit");
    FC_FeatureAttributeValue murAB = nature21
        .getFeatureAttributeValueByName("Mur anti-bruit");
    murAB
        .setDefinition("Mur destiné à protéger les riverains d’une voie de communication du bruit qu’elle engendre.");
    sProduit.createFeatureAttributeValue(nature21, "Pont");
    FC_FeatureAttributeValue pont = nature21
        .getFeatureAttributeValueByName("Pont");
    pont.setDefinition("Ouvrage d’art susceptible d’enjamber un ou plusieurs éléments du réseau routier, ferré ou hydrographique : pont, passerelle, pont isolé, pont mobile.");
    sProduit.createFeatureAttributeValue(nature21, "Ruines");
    FC_FeatureAttributeValue ruines = nature21
        .getFeatureAttributeValueByName("Ruines");
    ruines.setDefinition("Bâtiment en ruines.");
    sProduit.createFeatureAttributeValue(nature21, "Quai");
    FC_FeatureAttributeValue quai = nature21
        .getFeatureAttributeValueByName("Quai");
    quai.setDefinition("Muraille en maçonnerie ou enrochement, élevée le long d’un cours d’eau pour retenir les berges, pour empêcher les débordements. Rivage d’un port aménagé pour l’accostage des bateaux. Ouvrage en maçonnerie s’opposant à l’écoulement des eaux marines ou fluviales.");

    // Attribut Z_MIN
    sProduit.createFeatureAttribute(constructLine, "Z_MIN", "float", false);
    AttributeType zMin5 = constructLine.getFeatureAttributeByName("Z_MIN");
    zMin5
        .setDefinition("Altitude minimale d’un objet linéaire ou surfacique. Cette information est issue de la saisie photogrammétrique et concerne des objets dont les points initiaux et finaux ne sont pas caractéristiques de l’altitude de l’objet (objets surfaciques, objets linéaires isolés). Pour des objets surfaciques comme les bâtiments ou les réservoirs, il s’agit de l’altitude minimum du pourtour tel qu’il est défini en planimétrie (généralement l’altitude aux gouttières) et non de l’altitude minimum absolue.");

    // Attribut Z_MAX
    sProduit.createFeatureAttribute(constructLine, "Z_MAX", "float", false);
    AttributeType zMax5 = constructLine.getFeatureAttributeByName("Z_MAX");
    zMax5
        .setDefinition("Altitude maximale d’un objet linéaire ou surfacique. Cette information est issue de la saisie photogrammétrique et concerne des objets dont les points initiaux et finaux ne sont pas caractéristiques de l’altitude de l’objet (objets surfaciques, objets linéaires isolés). Pour des objets surfaciques comme les bâtiments ou les réservoirs, il s’agit de l’altitude maximum du pourtour tel qu’il est défini en planimétrie (généralement l’altitude aux gouttières) et non de l’altitude maximum absolue.");

    // Classe
    // Construction_Ponctuelle///////////////////////////////////////////////////

    sProduit.createFeatureType("CONSTRUCTION_PONCTUELLE");
    FeatureType constructPonct = (FeatureType) (sProduit
        .getFeatureTypeByName("CONSTRUCTION_PONCTUELLE"));
    constructPonct
        .setDefinition("Construction de faible emprise et de grande hauteur de plus de 50 m de haut et de moins de 20 m2.");
    constructPonct.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(constructPonct, "ID", "string", false);
    AttributeType id19 = constructPonct.getFeatureAttributeByName("ID");
    id19.setDefinition("Identifiant de la construction.Cet identifiant est unique. Il est stable d’une édition à l’autre. ");

    // Attribut PREC_PLANI
    sProduit
        .createFeatureAttribute(constructPonct, "PREC_PLANI", "float", true);
    AttributeType prec_plani17 = constructPonct
        .getFeatureAttributeByName("PREC_PLANI");
    prec_plani17
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani17, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani17, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani17, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani17, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani17, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani17, "30.0");

    // Attribut PREC_ALTI
    sProduit.createFeatureAttribute(constructPonct, "PREC_ALTI", "float", true);
    AttributeType prec_alti17 = constructPonct
        .getFeatureAttributeByName("PREC_ALTI");
    prec_alti17
        .setDefinition("Précision géométrique altimétrique. Attribut précisant la précision géométrique en altimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_alti17, "1.0");
    sProduit.createFeatureAttributeValue(prec_alti17, "2.5");
    sProduit.createFeatureAttributeValue(prec_alti17, "20.0");
    sProduit.createFeatureAttributeValue(prec_alti17, "9999.0");

    // Attribut NATURE
    sProduit.createFeatureAttribute(constructPonct, "NATURE", "string", true);
    AttributeType nature22 = constructPonct.getFeatureAttributeByName("NATURE");
    nature22
        .setDefinition("Attribut permettant de distinguer différentes natures de construction.");
    sProduit.createFeatureAttributeValue(nature22, "Indifférencié");
    FC_FeatureAttributeValue indif3 = nature22
        .getFeatureAttributeValueByName("Indifférencié");
    indif3
        .setDefinition("Construction de moins de 20 m2, de hauteur caractéristique, et non différenciée par une valeur d'attribut spécifique, ou point haut d'un bâtiment dont le contour a du être saisi au niveau du sol (ex. forme pyramidale) : clocher, flèche, minaret, sommet de pyramide, puit de pétrole. L'altitude de l'objet doit être largement supérieure à celle du sol ou à celle du bâtiment qui le supporte (10 m au minimum).");
    sProduit.createFeatureAttributeValue(nature22, "Antenne");
    FC_FeatureAttributeValue antenne = nature22
        .getFeatureAttributeValueByName("Antenne");
    antenne
        .setDefinition("Construction destinée à la réception ou à l'émission d'ondes électromagnétiques : antenne, pylône de télécommunication.");
    sProduit.createFeatureAttributeValue(nature22, "Cheminée");
    FC_FeatureAttributeValue cheminee = nature22
        .getFeatureAttributeValueByName("Cheminée");
    cheminee
        .setDefinition("Construction destinée à l'émission de fumées ou de gaz.");
    sProduit.createFeatureAttributeValue(nature22, "Eolienne");
    FC_FeatureAttributeValue eolienne = nature22
        .getFeatureAttributeValueByName("Eolienne");
    eolienne
        .setDefinition("Une éolienne est un dispositif permettant d'utiliser l'énergie éolienne pour produire de l'énergie mécanique ou bien de l'électricité. Seules les éoliennes récentes, destinées à produire de l’électricité, sont retenues.");
    sProduit.createFeatureAttributeValue(nature22, "Phare");
    FC_FeatureAttributeValue phare = nature22
        .getFeatureAttributeValueByName("Phare");
    phare
        .setDefinition("Tour élevée portant au sommet un foyer plus ou moins puissant destiné à guider les navires pendant la nuit. C'est le sommet du phare qui est indiqué. Il se superpose généralement à un objet de classe BATI_INDIFFERENCIE.");
    sProduit.createFeatureAttributeValue(nature22, "Torchère");
    FC_FeatureAttributeValue torchere = nature22
        .getFeatureAttributeValueByName("Torchère");
    torchere
        .setDefinition("Canalisation verticale par laquelle s'échappent et brûlent les résidus gazeux d'une raffinerie.");
    sProduit.createFeatureAttributeValue(nature22, "Transformateur");
    FC_FeatureAttributeValue transfo = nature22
        .getFeatureAttributeValueByName("Transformateur");
    transfo
        .setDefinition("Bâtiment technique dont la fonction est de transformer un système de courants variables en un ou plusieurs autres systèmes de courants variables de même fréquence, mais d’intensité ou de tension généralement différentes. Seuls les transformateurs de moins de 20m2 environ et de grande hauteur (ancien modèle) sont inclus.");

    // Attribut Z_MIN
    sProduit.createFeatureAttribute(constructPonct, "Z_MIN", "float", false);
    AttributeType zMin6 = constructPonct.getFeatureAttributeByName("Z_MIN");
    zMin6
        .setDefinition("Altitude minimale d’un objet linéaire ou surfacique. Cette information est issue de la saisie photogrammétrique et concerne des objets dont les points initiaux et finaux ne sont pas caractéristiques de l’altitude de l’objet (objets surfaciques, objets linéaires isolés). Pour des objets surfaciques comme les bâtiments ou les réservoirs, il s’agit de l’altitude minimum du pourtour tel qu’il est défini en planimétrie (généralement l’altitude aux gouttières) et non de l’altitude minimum absolue.");

    // Attribut Z_MAX
    sProduit.createFeatureAttribute(constructPonct, "Z_MAX", "float", false);
    AttributeType zMax6 = constructPonct.getFeatureAttributeByName("Z_MAX");
    zMax6
        .setDefinition("Altitude maximale d’un objet linéaire ou surfacique. Cette information est issue de la saisie photogrammétrique et concerne des objets dont les points initiaux et finaux ne sont pas caractéristiques de l’altitude de l’objet (objets surfaciques, objets linéaires isolés). Pour des objets surfaciques comme les bâtiments ou les réservoirs, il s’agit de l’altitude maximum du pourtour tel qu’il est défini en planimétrie (généralement l’altitude aux gouttières) et non de l’altitude maximum absolue.");

    // Classe
    // Construction_Surfacique///////////////////////////////////////////////////

    sProduit.createFeatureType("CONSTRUCTION_SURFACIQUE");
    FeatureType constructSurf = (FeatureType) (sProduit
        .getFeatureTypeByName("CONSTRUCTION_SURFACIQUE"));
    constructSurf
        .setDefinition("Ouvrage de grande surface lié au franchissement d’un obstacle par une voie de communication, ou à l’aménagement d’une rivière ou d’un canal.");
    constructSurf.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(constructSurf, "ID", "string", false);
    AttributeType id20 = constructSurf.getFeatureAttributeByName("ID");
    id20.setDefinition("Identifiant de la construction.Cet identifiant est unique. Il est stable d’une édition à l’autre. ");

    // Attribut PREC_PLANI
    sProduit.createFeatureAttribute(constructSurf, "PREC_PLANI", "float", true);
    AttributeType prec_plani18 = constructSurf
        .getFeatureAttributeByName("PREC_PLANI");
    prec_plani18
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani18, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani18, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani18, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani18, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani18, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani18, "30.0");

    // Attribut PREC_ALTI
    sProduit.createFeatureAttribute(constructSurf, "PREC_ALTI", "float", true);
    AttributeType prec_alti18 = constructSurf
        .getFeatureAttributeByName("PREC_ALTI");
    prec_alti18
        .setDefinition("Précision géométrique altimétrique. Attribut précisant la précision géométrique en altimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_alti18, "1.0");
    sProduit.createFeatureAttributeValue(prec_alti18, "2.5");
    sProduit.createFeatureAttributeValue(prec_alti18, "20.0");
    sProduit.createFeatureAttributeValue(prec_alti18, "9999.0");

    // Attribut NATURE
    sProduit.createFeatureAttribute(constructSurf, "NATURE", "string", true);
    AttributeType nature23 = constructSurf.getFeatureAttributeByName("NATURE");
    nature23
        .setDefinition("Attribut permettant de distinguer différentes natures de construction.");
    sProduit.createFeatureAttributeValue(nature23, "Barrage");
    FC_FeatureAttributeValue barrage2 = nature23
        .getFeatureAttributeValueByName("Barrage");
    barrage2
        .setDefinition("Grand barrage en maçonnerie apparente. Ex : barrage-voûte.");
    sProduit.createFeatureAttributeValue(nature23, "Dalle de protection");
    FC_FeatureAttributeValue dalle = nature23
        .getFeatureAttributeValueByName("Dalle de protection");
    dalle
        .setDefinition("Dalle (ou auvent) horizontale protégeant une voie de communication des chutes de pierres, des coulées de neige, ou protégeant le voisinage du bruit.");
    sProduit.createFeatureAttributeValue(nature23, "Ecluse");
    FC_FeatureAttributeValue ecluse = nature23
        .getFeatureAttributeValueByName("Ecluse");
    ecluse
        .setDefinition("Ouvrage hydraulique formé essentiellement de portes munies de vannes destiné à retenir ou à lâcher l’eau selon les besoins : ascenseur à bateaux, cale sèche, écluse, radoub.");
    sProduit.createFeatureAttributeValue(nature23, "Pont");
    FC_FeatureAttributeValue pont10 = nature23
        .getFeatureAttributeValueByName("Pont");
    pont10
        .setDefinition("Pont supportant plusieurs objets linéaires, un objet surfacique, ou pont dont l’emprise dépasse largement celle des voies qu’il supporte. Il peut être mobile.");
    sProduit.createFeatureAttributeValue(nature23, "Escalier");
    FC_FeatureAttributeValue escalier10 = nature23
        .getFeatureAttributeValueByName("Escalier");
    escalier10
        .setDefinition("Escalier monumental uniquement (contours de l’escalier).");

    // Attribut Z_MIN
    sProduit.createFeatureAttribute(constructSurf, "Z_MIN", "float", false);
    AttributeType zMin7 = constructSurf.getFeatureAttributeByName("Z_MIN");
    zMin7
        .setDefinition("Altitude minimale d’un objet linéaire ou surfacique. Cette information est issue de la saisie photogrammétrique et concerne des objets dont les points initiaux et finaux ne sont pas caractéristiques de l’altitude de l’objet (objets surfaciques, objets linéaires isolés). Pour des objets surfaciques comme les bâtiments ou les réservoirs, il s’agit de l’altitude minimum du pourtour tel qu’il est défini en planimétrie (généralement l’altitude aux gouttières) et non de l’altitude minimum absolue.");

    // Attribut Z_MAX
    sProduit.createFeatureAttribute(constructSurf, "Z_MAX", "float", false);
    AttributeType zMax7 = constructSurf.getFeatureAttributeByName("Z_MAX");
    zMax7
        .setDefinition("Altitude maximale d’un objet linéaire ou surfacique. Cette information est issue de la saisie photogrammétrique et concerne des objets dont les points initiaux et finaux ne sont pas caractéristiques de l’altitude de l’objet (objets surfaciques, objets linéaires isolés). Pour des objets surfaciques comme les bâtiments ou les réservoirs, il s’agit de l’altitude maximum du pourtour tel qu’il est défini en planimétrie (généralement l’altitude aux gouttières) et non de l’altitude maximum absolue.");

    /***************************************************************************
     * Ajout du thème Administratif
     **************************************************************************/
    // Classe Commune///////////////////////////////////////////////////

    sProduit.createFeatureType("COMMUNE");
    FeatureType commune = (FeatureType) (sProduit
        .getFeatureTypeByName("COMMUNE"));
    commune
        .setDefinition("Plus petite subdivision du territoire, administrée par un maire, des adjoints et un conseil municipal.");
    commune.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(commune, "ID", "string", false);
    AttributeType id21 = commune.getFeatureAttributeByName("ID");
    id21.setDefinition("Identifiant de la commune.");

    // Attribut PREC_PLANI
    sProduit.createFeatureAttribute(commune, "PREC_PLANI", "float", true);
    AttributeType prec_plani19 = commune
        .getFeatureAttributeByName("PREC_PLANI");
    prec_plani19
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani19, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani19, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani19, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani19, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani19, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani19, "30.0");

    // Attribut NOM
    sProduit.createFeatureAttribute(commune, "NOM", "string", false);
    AttributeType nom21 = commune.getFeatureAttributeByName("NOM");
    nom21
        .setDefinition("Nom officiel. Dénomination officielle (INSEE) de la commune, transcrite avec la graphie retenue par l’INSEE (majuscules, minuscules, accentuation, blancs, tirets). L’article est placé en tête du nom.");

    // Attribut CODE_INSEE
    sProduit.createFeatureAttribute(commune, "CODE_INSEE", "string", false);
    AttributeType codeInsee = commune.getFeatureAttributeByName("CODE_INSEE");
    codeInsee.setDefinition("Code INSEE de la commune sur 5 caractères.");

    // Attribut STATUT
    sProduit.createFeatureAttribute(commune, "STATUT", "string", true);
    AttributeType statut = commune.getFeatureAttributeByName("STATUT");
    statut.setDefinition("Précise le rôle le plus élevé joué par la commune.");
    sProduit.createFeatureAttributeValue(statut, "Capitale d'état");
    sProduit.createFeatureAttributeValue(statut, "Préfecture de région");
    sProduit.createFeatureAttributeValue(statut, "Préfecture");
    sProduit.createFeatureAttributeValue(statut, "Sous-préfecture");
    sProduit.createFeatureAttributeValue(statut, "Chef-lieu de canton");
    sProduit.createFeatureAttributeValue(statut, "Commune simple");

    // Attribut CANTON
    sProduit.createFeatureAttribute(commune, "CANTON", "string", false);
    AttributeType canton = commune.getFeatureAttributeByName("CANTON");
    canton
        .setDefinition("Nom du canton de rattachement.Le canton est une unité de découpage non électoral du territoire. Il s’agit d’un canton au sens INSEE du terme : ce peut être un regroupement de communes ou une commune.");

    // Attribut ARRONDISST
    sProduit.createFeatureAttribute(commune, "ARRONDISST", "string", false);
    AttributeType arrondisst = commune.getFeatureAttributeByName("ARRONDISST");
    arrondisst
        .setDefinition("Nom de l’arrondissement de rattachement de la commune. L’arrondissement est une unité administrative située hiérarchiquement entre la commune et le département.");

    // Attribut DEPART
    sProduit.createFeatureAttribute(commune, "DEPART", "string", false);
    AttributeType depart = commune.getFeatureAttributeByName("DEPART");
    depart.setDefinition("Nom du département de rattachement de la commune.");

    // Attribut REGION
    sProduit.createFeatureAttribute(commune, "REGION", "string", false);
    AttributeType region = commune.getFeatureAttributeByName("REGION");
    region.setDefinition("Nom de la région de rattachement de la commune.");

    // Attribut POPUL
    sProduit.createFeatureAttribute(commune, "POPUL", "integer", false);
    AttributeType popul = commune.getFeatureAttributeByName("POPUL");
    popul
        .setDefinition("Population de la commune. Population sans « double compte » de la commune en nombre d’habitants : certaines catégories de population (militaires, prisonniers, étudiants, etc.) recensées dans plusieurs lieux d’activité ou de résidence ne sont comptées qu’une seule fois.");

    // Attribut MULTICAN
    sProduit.createFeatureAttribute(commune, "MULTICAN", "string", true);
    AttributeType multican = commune.getFeatureAttributeByName("MULTICAN");
    multican
        .setDefinition("Multi canton.Précise si la commune est constituée de plusieurs cantons.");
    sProduit.createFeatureAttributeValue(multican, "Oui");
    sProduit.createFeatureAttributeValue(multican, "Non");

    // Classe
    // Arrondissement///////////////////////////////////////////////////

    sProduit.createFeatureType("ARRONDISSEMENT");
    FeatureType arrondissement = (FeatureType) (sProduit
        .getFeatureTypeByName("ARRONDISSEMENT"));
    arrondissement
        .setDefinition("Arrondissement municipal : subdivision administrative de certaines communes. Les arrondissements municipaux sont gérés par l’INSEE comme des communes.");
    arrondissement.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(arrondissement, "ID", "string", false);
    AttributeType id22 = arrondissement.getFeatureAttributeByName("ID");
    id22.setDefinition("Identifiant de l'arrondissement.");

    // Attribut PREC_PLANI
    sProduit
        .createFeatureAttribute(arrondissement, "PREC_PLANI", "float", true);
    AttributeType prec_plani20 = arrondissement
        .getFeatureAttributeByName("PREC_PLANI");
    prec_plani20
        .setDefinition("Précision géométrique planimétrique. Attribut précisant la précision géométrique en planimétrie de la donnée.");
    sProduit.createFeatureAttributeValue(prec_plani20, "0.5");
    sProduit.createFeatureAttributeValue(prec_plani20, "1.5");
    sProduit.createFeatureAttributeValue(prec_plani20, "2.5");
    sProduit.createFeatureAttributeValue(prec_plani20, "5.0");
    sProduit.createFeatureAttributeValue(prec_plani20, "10.0");
    sProduit.createFeatureAttributeValue(prec_plani20, "30.0");

    // Attribut NOM
    sProduit.createFeatureAttribute(arrondissement, "NOM", "string", false);
    AttributeType nom22 = arrondissement.getFeatureAttributeByName("NOM");
    nom22
        .setDefinition("Nom officiel. Dénomination officielle (INSEE) de la commune d’appartenance de l’arrondissement, transcrite avec la graphie retenue par l’INSEE (majuscules, minuscules, accentuation, blancs, tirets). L’article est placé en tête du nom.");

    // Attribut CODE_INSEE
    sProduit.createFeatureAttribute(arrondissement, "CODE_INSEE", "string",
        false);
    AttributeType codeInsee2 = arrondissement
        .getFeatureAttributeByName("CODE_INSEE");
    codeInsee2
        .setDefinition("Code INSEE de l'arrondissement sur 5 caractères.");

    // Classe CHEF_LIEU///////////////////////////////////////////////////

    sProduit.createFeatureType("CHEF_LIEU");
    FeatureType chefLieu = (FeatureType) (sProduit
        .getFeatureTypeByName("CHEF_LIEU"));
    chefLieu
        .setDefinition("Toponyme de la zone d’habitat dans laquelle se trouve la mairie de la commune. Dans certains cas, le chef-lieu n’est pas dans la commune.");
    chefLieu.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(chefLieu, "ID", "string", false);
    AttributeType id23 = chefLieu.getFeatureAttributeByName("ID");
    id23.setDefinition("Identifiant du chef-lieu de commune commune.");

    // Attribut ORIGIN_NOM
    sProduit.createFeatureAttribute(chefLieu, "ORIGINE_NOM", "string", true);
    AttributeType originnom23 = chefLieu
        .getFeatureAttributeByName("ORIGINE_NOM");
    originnom23.setDefinition("Origine du toponyme.");
    sProduit.createFeatureAttributeValue(originnom23, "Scan25");
    FC_FeatureAttributeValue scan23 = originnom23
        .getFeatureAttributeValueByName("Scan25");
    scan23.setDefinition("Carte IGN au 1 : 25 000.");
    sProduit.createFeatureAttributeValue(originnom23, "BDCarto");
    FC_FeatureAttributeValue bdcarto23 = originnom23
        .getFeatureAttributeValueByName("BDCarto");
    bdcarto23
        .setDefinition("Base de données BD CARTO® pour la géométrie de l’objet.");
    sProduit.createFeatureAttributeValue(originnom23, "BDTopo");
    FC_FeatureAttributeValue bdtopo23 = originnom23
        .getFeatureAttributeValueByName("BDTopo");
    bdtopo23
        .setDefinition("Base de données BD TOPO® antérieure à la BD TOPO® Pays.");
    sProduit.createFeatureAttributeValue(originnom23, "BDNyme");
    FC_FeatureAttributeValue bdnyme23 = originnom23
        .getFeatureAttributeValueByName("BDNyme");
    bdnyme23.setDefinition("Base de données BD NYME®.");
    sProduit.createFeatureAttributeValue(originnom23, "Géoroute");
    FC_FeatureAttributeValue georoute23 = originnom23
        .getFeatureAttributeValueByName("Géoroute");
    georoute23
        .setDefinition("Base de données GEOROUTE® pour la géométrie de l’objet (notamment les points et surfaces d’activité sur les zones couvertes par GEOROUTE®).");
    sProduit.createFeatureAttributeValue(originnom23, "Fichier");
    FC_FeatureAttributeValue fichier23 = originnom23
        .getFeatureAttributeValueByName("Fichier");
    fichier23
        .setDefinition("Fichier numérique obtenu auprès d’un prestataire extérieur à l’IGN");
    sProduit.createFeatureAttributeValue(originnom23, "Plan");
    FC_FeatureAttributeValue plan23 = originnom23
        .getFeatureAttributeValueByName("Plan");
    plan23
        .setDefinition("Plan qui a été reporté ou documentation aidant à la localisation.");
    sProduit.createFeatureAttributeValue(originnom23, "BDParcellaire");
    FC_FeatureAttributeValue bdparcel23 = originnom23
        .getFeatureAttributeValueByName("BDParcellaire");
    bdparcel23.setDefinition("Base de données BD PARCELLAIRE®.");
    sProduit.createFeatureAttributeValue(originnom23, "Terrain");
    FC_FeatureAttributeValue terrain23 = originnom23
        .getFeatureAttributeValueByName("Terrain");
    terrain23
        .setDefinition("Information provenant d’un passage sur le terrain");
    sProduit.createFeatureAttributeValue(originnom23, "NR");
    FC_FeatureAttributeValue nr23 = originnom23
        .getFeatureAttributeValueByName("NR");
    nr23.setDefinition("Non renseigné");

    // Attribut NATURE
    sProduit.createFeatureAttribute(chefLieu, "NATURE", "string", true);
    AttributeType nature24 = chefLieu.getFeatureAttributeByName("NATURE");
    nature24.setDefinition("Nature du chef-lieu.");
    sProduit.createFeatureAttributeValue(nature24, "Capitale d'état");
    sProduit.createFeatureAttributeValue(nature24, "Préfecture de région");
    sProduit.createFeatureAttributeValue(nature24, "Préfecture");
    sProduit.createFeatureAttributeValue(nature24, "Sous-préfecture");
    sProduit.createFeatureAttributeValue(nature24, "Canton");
    sProduit.createFeatureAttributeValue(nature24, "Commune");

    // Attribut NOM
    sProduit.createFeatureAttribute(chefLieu, "NOM", "string", false);
    AttributeType nom23 = chefLieu.getFeatureAttributeByName("NOM");
    nom23
        .setDefinition("Dénomination du chef-lieu de commune validée par le bureau de Toponymie.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(chefLieu, "IMPORTANCE", "string", true);
    AttributeType importance23 = chefLieu
        .getFeatureAttributeByName("IMPORTANCE");
    importance23
        .setDefinition("Importance du toponyme dans son environnement.");
    sProduit.createFeatureAttributeValue(importance23, "1");
    FC_FeatureAttributeValue un23 = importance23
        .getFeatureAttributeValueByName("1");
    un23.setDefinition("Lieu habité de plus de 100 000 habitants.");
    sProduit.createFeatureAttributeValue(importance23, "2");
    FC_FeatureAttributeValue deux23 = importance23
        .getFeatureAttributeValueByName("2");
    deux23.setDefinition("Lieu habité de 25 000 à 100 000 habitants.");
    sProduit.createFeatureAttributeValue(importance23, "3");
    FC_FeatureAttributeValue trois23 = importance23
        .getFeatureAttributeValueByName("3");
    trois23.setDefinition("Lieu habité de 5000 à 25 000 habitants.");
    sProduit.createFeatureAttributeValue(importance23, "4");
    FC_FeatureAttributeValue quatre23 = importance23
        .getFeatureAttributeValueByName("4");
    quatre23.setDefinition("Lieu habité de 1000 à 5000 habitants.");
    sProduit.createFeatureAttributeValue(importance23, "5");
    FC_FeatureAttributeValue cinq23 = importance23
        .getFeatureAttributeValueByName("5");
    cinq23.setDefinition("Lieu habité de 200 à 1000 habitants.");
    sProduit.createFeatureAttributeValue(importance23, "6");
    FC_FeatureAttributeValue six23 = importance23
        .getFeatureAttributeValueByName("6");
    six23
        .setDefinition("Lieu habité de moins de 200 habitants, quartier de ville.");
    sProduit.createFeatureAttributeValue(importance23, "7");
    FC_FeatureAttributeValue sept23 = importance23
        .getFeatureAttributeValueByName("7");
    sept23
        .setDefinition("Groupe d'habitations (2 à 10 feux, 4 à 20 bâtiments, petit quartier de ville).");
    sProduit.createFeatureAttributeValue(importance23, "8");
    FC_FeatureAttributeValue huit23 = importance23
        .getFeatureAttributeValueByName("8");
    huit23.setDefinition("Constructions isolées (1 feu, 1 à 3 bâtiments).");
    sProduit.createFeatureAttributeValue(importance23, "NC");
    FC_FeatureAttributeValue nc23 = importance23
        .getFeatureAttributeValueByName("NC");
    nc23.setDefinition("Non concerné.");
    sProduit.createFeatureAttributeValue(importance23, "NR");
    FC_FeatureAttributeValue nr24 = importance23
        .getFeatureAttributeValueByName("NR");
    nr24.setDefinition("Non renseigné.");

    // Je remplace l'attribut ID_COM (clé étrangère de la commune de
    // rattachement) par une association
    sProduit.createFeatureAssociation("chef-lieu", chefLieu, commune,
        "est chef-lieu de", "a pour chef-lieu");

    /***************************************************************************
     * Ajout du thème Zone d'activité
     **************************************************************************/

    // Classe
    // SURFACE_ACTIVITE///////////////////////////////////////////////////
    sProduit.createFeatureType("SURFACE_ACTIVITE");
    FeatureType surfActiv = (FeatureType) (sProduit
        .getFeatureTypeByName("SURFACE_ACTIVITE"));
    surfActiv
        .setDefinition("Enceinte d’un équipement public, d’un site ou d’une zone ayant un caractère administratif, culturel, sportif, industriel ou commercial.");
    surfActiv.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(surfActiv, "ID", "string", false);
    AttributeType id24 = surfActiv.getFeatureAttributeByName("ID");
    id24.setDefinition("Identifiant de la surface.");

    // Attribut ORIGINE
    sProduit.createFeatureAttribute(surfActiv, "ORIGINE", "string", true);
    AttributeType origine24 = surfActiv.getFeatureAttributeByName("ORIGINE");
    origine24.setDefinition("Source de l'information.");
    sProduit.createFeatureAttributeValue(origine24, "Plan");
    sProduit.createFeatureAttributeValue(origine24, "Fichier");
    sProduit.createFeatureAttributeValue(origine24, "Terrain");
    sProduit.createFeatureAttributeValue(origine24, "Scan25");
    sProduit.createFeatureAttributeValue(origine24, "Orthophotographie");
    sProduit.createFeatureAttributeValue(origine24, "BDTopo");
    sProduit.createFeatureAttributeValue(origine24, "BDCarto");
    sProduit.createFeatureAttributeValue(origine24, "Géoroute");
    sProduit.createFeatureAttributeValue(origine24, "BDNyme");
    sProduit.createFeatureAttributeValue(origine24, "Calculé");
    sProduit.createFeatureAttributeValue(origine24, "BDParcellaire");
    sProduit.createFeatureAttributeValue(origine24, "NR");

    // Attribut CATEGORIE
    sProduit.createFeatureAttribute(surfActiv, "CATEGORIE", "string", true);
    AttributeType categorie24 = surfActiv
        .getFeatureAttributeByName("CATEGORIE");
    categorie24
        .setDefinition("Attribut permettant de préciser le type de l’activité concernée par l’enceinte.");
    sProduit.createFeatureAttributeValue(categorie24, "Administratif");
    FC_FeatureAttributeValue admin24 = categorie24
        .getFeatureAttributeValueByName("Administratif");
    admin24
        .setDefinition("Établissement, site ou zone ayant un caractère public ou administratif : bureau ou hôtel des postes, caserne de pompiers, divers public ou administratif, enceinte militaire, établissement pénitentiaire, gendarmerie, hôtel de département, hôtel de région, mairie, maison forestière, palais de justice, poste ou hôtel de police, préfecture sous-préfecture. La fonction publique ou administrative de l’établissement doit être actuelle.");
    sProduit.createFeatureAttributeValue(categorie24, "Culture et loisirs");
    FC_FeatureAttributeValue cult24 = categorie24
        .getFeatureAttributeValueByName("Culture et loisirs");
    cult24
        .setDefinition("Établissement ou lieu spécialement aménagé pour une activité culturelle ou de loisirs : camping, maison du parc, musée, parc de loisirs, parc zoologique, village de vacances.");
    sProduit.createFeatureAttributeValue(categorie24, "Enseignement");
    FC_FeatureAttributeValue enseignement24 = categorie24
        .getFeatureAttributeValueByName("Enseignement");
    enseignement24
        .setDefinition("Etablissement d’enseignement : enseignement primaire, enseignement secondaire, enseignement supérieur. Tous les établissements d’enseignement publics, confessionnels ou privés ayant un contrat simple ou d’association avec l’Etat sont inclus. Les crèches, les cours du soir, les cités et les restaurants universitaires sont exclus.");
    sProduit.createFeatureAttributeValue(categorie24, "Gestion des eaux");
    FC_FeatureAttributeValue eaux24 = categorie24
        .getFeatureAttributeValueByName("Gestion des eaux");
    eaux24
        .setDefinition("Construction ou site liés à l’approvisionnement, au traitement de l’eau pour différents besoins (agricole, industriel, consommation) ou à l’épuration des eaux usées avant rejet dans la nature : station de pompage, usine de traitement des eaux.");
    sProduit.createFeatureAttributeValue(categorie24,
        "Industriel et commercial");
    FC_FeatureAttributeValue indus24 = categorie24
        .getFeatureAttributeValueByName("Industriel et commercial");
    indus24
        .setDefinition("Bâtiment, site ou zone à caractère industriel ou commercial : aquaculture, carrière, centrale électrique, divers commercial, divers industriel, haras national, marais salants, marché, mine, usine. Les sites ayant perdu leur fonction industrielle ou commerciale sont exclus (ancienne usine, ancienne carrière).");
    sProduit.createFeatureAttributeValue(categorie24, "Santé");
    FC_FeatureAttributeValue sante24 = categorie24
        .getFeatureAttributeValueByName("Santé");
    sante24
        .setDefinition("Établissement thermal ou de type hospitalier : établissement hospitalier, établissement thermal.");
    sProduit.createFeatureAttributeValue(categorie24, "Sport");
    FC_FeatureAttributeValue sport24 = categorie24
        .getFeatureAttributeValueByName("Sport");
    sport24
        .setDefinition("Établissement ou lieu spécialement aménagé pour la pratique d’une ou de plusieurs activités sportives : golf, hippodrome, piscine, stade.");
    sProduit.createFeatureAttributeValue(categorie24, "Transport");
    FC_FeatureAttributeValue transport24 = categorie24
        .getFeatureAttributeValueByName("Transport");
    transport24
        .setDefinition("Bâtiment ou site lié à une activité de transport routier, ferré ou aérien : aérodrome, aire d’autoroute, gare voyageurs uniquement, gare voyageurs et fret, gare fret uniquement, péage. La surface d’activité avec un PAI de nature péage est superposée à la surface de route de nature péage, aux différences de saisie près.");

    // Classe
    // PAI_ADMINISTRATIF_MILITAIRE///////////////////////////////////////////////////

    sProduit.createFeatureType("PAI_ADMINISTRATIF_MILITAIRE");
    FeatureType paiAdminMilit = (FeatureType) (sProduit
        .getFeatureTypeByName("PAI_ADMINISTRATIF_MILITAIRE"));
    paiAdminMilit
        .setDefinition("Désignation d’un établissement, site ou zone ayant un caractère public ou administratif ou militaire.");
    paiAdminMilit.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(paiAdminMilit, "ID", "string", false);
    AttributeType id25 = paiAdminMilit.getFeatureAttributeByName("ID");
    id25.setDefinition("Identifiant du PAI. Cet identifiant est unique. Il est stable d’une édition à l’autre.");

    // Attribut ORIGINE
    sProduit.createFeatureAttribute(paiAdminMilit, "ORIGINE", "string", true);
    AttributeType origine25 = paiAdminMilit
        .getFeatureAttributeByName("ORIGINE");
    origine25.setDefinition("Source de l'information.");
    sProduit.createFeatureAttributeValue(origine25, "Plan");
    sProduit.createFeatureAttributeValue(origine25, "Fichier");
    sProduit.createFeatureAttributeValue(origine25, "Terrain");
    sProduit.createFeatureAttributeValue(origine25, "Scan25");
    sProduit.createFeatureAttributeValue(origine25, "Orthophotographie");
    sProduit.createFeatureAttributeValue(origine25, "BDTopo");
    sProduit.createFeatureAttributeValue(origine25, "BDCarto");
    sProduit.createFeatureAttributeValue(origine25, "Géoroute");
    sProduit.createFeatureAttributeValue(origine25, "BDNyme");
    sProduit.createFeatureAttributeValue(origine25, "Calculé");
    sProduit.createFeatureAttributeValue(origine25, "BDParcellaire");
    sProduit.createFeatureAttributeValue(origine25, "NR");

    // Attribut NATURE
    sProduit.createFeatureAttribute(paiAdminMilit, "NATURE", "string", true);
    AttributeType nature25 = paiAdminMilit.getFeatureAttributeByName("NATURE");
    nature25.setDefinition("Nature du PAI.");
    sProduit.createFeatureAttributeValue(nature25, "Borne");
    FC_FeatureAttributeValue borne25 = nature25
        .getFeatureAttributeValueByName("Borne");
    borne25
        .setDefinition("Borne nommée : borne frontière, point de triangulation, point frontière.");
    sProduit.createFeatureAttributeValue(nature25,
        "Bureau de poste ou hôtel des postes");
    FC_FeatureAttributeValue poste25 = nature25
        .getFeatureAttributeValueByName("Bureau de poste ou hôtel des postes");
    poste25
        .setDefinition("Bureau de poste ouvert au public : Bureau de poste, hôtel des postes. Seuls les bureaux de poste ouverts en permanence sont inclus. En général, dans les agglomérations, seules les postes centrales sont incluses.");
    sProduit.createFeatureAttributeValue(nature25, "Caserne de pompiers");
    FC_FeatureAttributeValue caserne25 = nature25
        .getFeatureAttributeValueByName("Caserne de pompiers");
    caserne25
        .setDefinition("Bâtiment ayant ou non un bureau ou une permanence et qui est entièrement concerné par l'activité du corps des Sapeurs-Pompiers.");
    sProduit.createFeatureAttributeValue(nature25,
        "Divers public ou administratif");
    FC_FeatureAttributeValue divers25 = nature25
        .getFeatureAttributeValueByName("Divers public ou administratif");
    divers25
        .setDefinition("Bâtiment ou zone à caractère public ou administratif, qui n’est ni défini par une autre classe de PAI, ni par une autre valeur d’attribut NATURE de la présente classe (administratif ou militaire) : UNESCO, Parlement Européen, ministère, direction ministérielle, Assemblée nationale, Sénat, cité administrative, poste de douane, capitainerie, salle de spectacle,etc. En général les établissements et les sites retenus ont une importance ou une notoriété d’ordre national ou régional ou une surface au sol d’au moins 1000 m2 environ.");
    sProduit.createFeatureAttributeValue(nature25, "Enceinte militaire");
    FC_FeatureAttributeValue enceinte25 = nature25
        .getFeatureAttributeValueByName("Enceinte militaire");
    enceinte25
        .setDefinition("Zone en permanence réservée pour les rassemblements de troupes de toutes les armes, soit pour des manoeuvres, des exercices (camp d’instruction), soit pour des essais, des études : base, camp, caserne, dépôt de matériels, terrain permanent d’entraînement, caserne de CRS, caserne de gendarmes mobiles, etc. Les champs de tir sont exclus ainsi que les propriétés de l’armée qui ne sont indiquées d’aucune manière sur le terrain (ni clôtures, ni barrière, ni pancartes,…) et ne faisant l’objet d’aucune restriction particulière.");
    sProduit.createFeatureAttributeValue(nature25,
        "Etablissement pénitenciaire");
    FC_FeatureAttributeValue prison25 = nature25
        .getFeatureAttributeValueByName("Etablissement pénitenciaire");
    prison25
        .setDefinition("Établissement clos aménagé pour recevoir des délinquants condamnés à une peine privative de liberté ou des détenus en instance de jugement : maison d’arrêt, prison. Les annexes sont exclues.");
    sProduit.createFeatureAttributeValue(nature25, "Gendarmerie");
    FC_FeatureAttributeValue gendarmerie25 = nature25
        .getFeatureAttributeValueByName("Gendarmerie");
    gendarmerie25
        .setDefinition("Caserne où les gendarmes sont logés ; bureaux où ils remplissent leurs fonctions administratives : gendarmerie, gendarmerie d’autoroute. Définition de l’emprise du site : surface de l’ensemble de la caserne, généralement délimitée par une clôture et incluant logements et bureaux.");
    sProduit.createFeatureAttributeValue(nature25, "Hôtel de département");
    FC_FeatureAttributeValue dep25 = nature25
        .getFeatureAttributeValueByName("Hôtel de département");
    dep25
        .setDefinition("Bâtiment où siège le conseil général. Seul le bâtiment principal est inclus. Les annexes ne le sont pas, sauf éventuellement une annexe située dans une autre agglomération lorsqu’elle a une fonction proche de celle du siège.");
    sProduit.createFeatureAttributeValue(nature25, "Hôtel de région");
    FC_FeatureAttributeValue reg25 = nature25
        .getFeatureAttributeValueByName("Hôtel de région");
    reg25
        .setDefinition("Bâtiment où siège le conseil régional. Seul le bâtiment principal est inclus. Les annexes ne le sont pas, sauf éventuellement une annexe située dans une autre agglomération lorsqu’elle a une fonction proche de celle du siège.");
    sProduit.createFeatureAttributeValue(nature25, "Mairie");
    FC_FeatureAttributeValue mairie25 = nature25
        .getFeatureAttributeValueByName("Mairie");
    mairie25
        .setDefinition("Bâtiment où se trouvent le bureau du maire, les services de l’administration municipale et où siège normalement le conseil municipal : mairie, mairie annexe, hôtel de ville. Les mairies annexes sont incluses (fréquentes dans les grandes villes ou dans les anciens chefs-lieux de commune ayant fusionné, elles offrent des services similaires aux mairies principales). Les annexes de la mairie (services techniques,…) sont exclues. En général le bâtiment saisi est celui de l’accueil du public.");
    sProduit.createFeatureAttributeValue(nature25, "Maison forestière");
    FC_FeatureAttributeValue maison25 = nature25
        .getFeatureAttributeValueByName("Maison forestière");
    maison25
        .setDefinition("Maison gérée par l’office national des forêts. Les maisons de garde occupées par au moins un agent de l’ONF sont incluses. Les bureaux de l’ONF, les domiciles d’agents servant aussi de bureau, sont exclus lorsqu’ils ne sont pas situés dans une maison forestière.");
    sProduit.createFeatureAttributeValue(nature25, "Ouvrage militaire");
    FC_FeatureAttributeValue ouvrage25 = nature25
        .getFeatureAttributeValueByName("Ouvrage militaire");
    ouvrage25.setDefinition("Ouvrages et installations militaires.");
    sProduit.createFeatureAttributeValue(nature25, "Palais de justice");
    FC_FeatureAttributeValue palais25 = nature25
        .getFeatureAttributeValueByName("Palais de justice");
    palais25
        .setDefinition("Bâtiment où l’on rend la justice : palais de justice, tribunal. Seule la justice pénale est traitée. Les tribunaux administratifs sont exclus.");
    sProduit.createFeatureAttributeValue(nature25,
        "Poste de police ou hôtel de police");
    FC_FeatureAttributeValue police25 = nature25
        .getFeatureAttributeValueByName("Poste de police ou hôtel de police");
    police25
        .setDefinition("Établissement occupé par un commissaire de police (officier de police judiciaire chargé de faire observer les règlements de police et de veiller au maintien de la paix publique) : hôtel de police nationale, commissariat, CRS d’autoroute, de port ou d’aéroport. Les bâtiments hébergeant uniquement la police municipale sont exclus. Les casernes de CRS et de gendarmes mobiles prennent la valeur « enceinte militaire » et les gendarmeries la valeur « gendarmerie ».");
    sProduit.createFeatureAttributeValue(nature25, "Préfecture");
    FC_FeatureAttributeValue pref25 = nature25
        .getFeatureAttributeValueByName("Préfecture");
    pref25
        .setDefinition("Établissement qui abrite l'ensemble des services de l'administration préfectorale. Les annexes sont exclues.");
    sProduit.createFeatureAttributeValue(nature25, "Préfecture de région");
    FC_FeatureAttributeValue prefReg25 = nature25
        .getFeatureAttributeValueByName("Préfecture de région");
    prefReg25
        .setDefinition("Établissement qui abrite le siège de l’administration civile de la région. Les annexes sont exclues.");
    sProduit.createFeatureAttributeValue(nature25, "Sous-préfecture");
    FC_FeatureAttributeValue sousPref25 = nature25
        .getFeatureAttributeValueByName("Sous-préfecture");
    sousPref25
        .setDefinition("Établissement qui abrite les services administratifs du sous-préfet. Les annexes sont exclues.");

    // Attribut TOPONYME
    sProduit.createFeatureAttribute(paiAdminMilit, "TOPONYME", "string", false);
    AttributeType top25 = paiAdminMilit.getFeatureAttributeByName("TOPONYME");
    top25
        .setDefinition("Nom validé par l’Equipe Produit Toponymie qui garantit son orthographe.");

    // Attribut IMPORTANCE
    sProduit
        .createFeatureAttribute(paiAdminMilit, "IMPORTANCE", "string", true);
    AttributeType importance25 = paiAdminMilit
        .getFeatureAttributeByName("IMPORTANCE");
    importance25
        .setDefinition("Importance du toponyme dans son environnement.");
    sProduit.createFeatureAttributeValue(importance25, "1");
    sProduit.createFeatureAttributeValue(importance25, "2");
    sProduit.createFeatureAttributeValue(importance25, "3");
    sProduit.createFeatureAttributeValue(importance25, "4");
    sProduit.createFeatureAttributeValue(importance25, "5");
    sProduit.createFeatureAttributeValue(importance25, "6");
    sProduit.createFeatureAttributeValue(importance25, "7");
    sProduit.createFeatureAttributeValue(importance25, "8");
    sProduit.createFeatureAttributeValue(importance25, "NC");
    sProduit.createFeatureAttributeValue(importance25, "NR");

    // Classe
    // PAI_CULTURE_LOISIRS///////////////////////////////////////////////////

    sProduit.createFeatureType("PAI_CULTURE_LOISIRS");
    FeatureType paiCultLoisirs = (FeatureType) (sProduit
        .getFeatureTypeByName("PAI_CULTURE_LOISIRS"));
    paiCultLoisirs
        .setDefinition("Désignation d’un établissement ou lieu spécialement aménagé pour une activité culturelle, touristique ou de loisirs.");
    paiCultLoisirs.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(paiCultLoisirs, "ID", "string", false);
    AttributeType id26 = paiCultLoisirs.getFeatureAttributeByName("ID");
    id26.setDefinition("Identifiant du PAI. Cet identifiant est unique. Il est stable d’une édition à l’autre.");

    // Attribut ORIGINE
    sProduit.createFeatureAttribute(paiCultLoisirs, "ORIGINE", "string", true);
    AttributeType origine26 = paiCultLoisirs
        .getFeatureAttributeByName("ORIGINE");
    origine26.setDefinition("Source de l'information.");
    sProduit.createFeatureAttributeValue(origine26, "Plan");
    sProduit.createFeatureAttributeValue(origine26, "Fichier");
    sProduit.createFeatureAttributeValue(origine26, "Terrain");
    sProduit.createFeatureAttributeValue(origine26, "Scan25");
    sProduit.createFeatureAttributeValue(origine26, "Orthophotographie");
    sProduit.createFeatureAttributeValue(origine26, "BDTopo");
    sProduit.createFeatureAttributeValue(origine26, "BDCarto");
    sProduit.createFeatureAttributeValue(origine26, "Géoroute");
    sProduit.createFeatureAttributeValue(origine26, "BDNyme");
    sProduit.createFeatureAttributeValue(origine26, "Calculé");
    sProduit.createFeatureAttributeValue(origine26, "BDParcellaire");
    sProduit.createFeatureAttributeValue(origine26, "NR");

    // Attribut NATURE
    sProduit.createFeatureAttribute(paiCultLoisirs, "NATURE", "string", true);
    AttributeType nature26 = paiCultLoisirs.getFeatureAttributeByName("NATURE");
    nature26.setDefinition("Nature du PAI.");
    sProduit.createFeatureAttributeValue(nature26, "Camping");
    FC_FeatureAttributeValue camping26 = nature26
        .getFeatureAttributeValueByName("Camping");
    camping26
        .setDefinition("Emplacement aménagé pour la pratique du camping d’une superficie de plus de 2 ha.");
    sProduit.createFeatureAttributeValue(nature26, "Construction");
    FC_FeatureAttributeValue construction26 = nature26
        .getFeatureAttributeValueByName("Construction");
    construction26
        .setDefinition("Construction nommée habitée ou associée à un groupe d’habitations : construction diverse, pigeonnier, moulin à vent.");
    sProduit.createFeatureAttributeValue(nature26, "Digue");
    FC_FeatureAttributeValue digue26 = nature26
        .getFeatureAttributeValueByName("Digue");
    digue26.setDefinition("Digue, môle, jetée.");
    sProduit.createFeatureAttributeValue(nature26, "Dolmen");
    FC_FeatureAttributeValue dolmen26 = nature26
        .getFeatureAttributeValueByName("Dolmen");
    dolmen26
        .setDefinition("Monument mégalithique formé d’une grande pierre plate posée sur d’autres pierres dressées verticalement. Les allées couvertes sont incluses.");
    sProduit.createFeatureAttributeValue(nature26, "Espace public");
    FC_FeatureAttributeValue espace26 = nature26
        .getFeatureAttributeValueByName("Espace public");
    espace26
        .setDefinition("Large espace découvert urbain désigné par un toponyme où aboutissent plusieurs rues, fermé à la circulation automobile, constituant un lieu remarquable : place, square, jardin, parc, parc communal, parc intercommunal, parc départemental, parc interdépartemental. Seuls les espaces publics possédant un toponyme sont retenus. Les parcs à vocation commerciale sont exclus (voir la valeur « Parc de loisirs » ci-dessous), de même que les parcs naturels (réserves, parcs nationaux, parcs naturels régionaux) qui sont traités en PAI_ESPACE_NATUREL.");
    sProduit.createFeatureAttributeValue(nature26, "Habitation troglodytique");
    FC_FeatureAttributeValue troglo26 = nature26
        .getFeatureAttributeValueByName("Habitation troglodytique");
    troglo26
        .setDefinition("Excavation naturelle ou creusée dans le roc (caverne, grotte), habitée ou anciennement habitée.");
    sProduit.createFeatureAttributeValue(nature26, "Maison du parc");
    FC_FeatureAttributeValue maison26 = nature26
        .getFeatureAttributeValueByName("Maison du parc");
    maison26
        .setDefinition("Bâtiment ouvert au public et géré par un Parc National ou Régional.");
    sProduit.createFeatureAttributeValue(nature26, "Menhir");
    FC_FeatureAttributeValue menhir26 = nature26
        .getFeatureAttributeValueByName("Menhir");
    menhir26
        .setDefinition("Pierre allongée, dressée verticalement. Les alignements en cromlech sont inclus.");
    sProduit.createFeatureAttributeValue(nature26, "Monument");
    FC_FeatureAttributeValue monument26 = nature26
        .getFeatureAttributeValueByName("Monument");
    monument26
        .setDefinition("Monument sans caractère religieux particulier : monument, statue, stèle.");
    sProduit.createFeatureAttributeValue(nature26, "Musée");
    FC_FeatureAttributeValue musee26 = nature26
        .getFeatureAttributeValueByName("Musée");
    musee26
        .setDefinition("Etablissement ouvert au public exposant une grande collection d'objets, de documents, etc., relatifs aux arts et aux sciences et pouvant servir a leur histoire. Sont inclus : tous les musees controles ou supervises par le ministere de la Culture (musees nationaux, classes, controles,¡K). les musees relevant de certains ministeres techniques ou de l¡¦assistance publique (musee de l¡¦armee, de la marine). les musees prives ou associatifs ayant une grande notoriete. les ecomusees.");
    sProduit.createFeatureAttributeValue(nature26, "Parc des expositions");
    FC_FeatureAttributeValue parc26 = nature26
        .getFeatureAttributeValueByName("Parc des expositions");
    parc26
        .setDefinition("Lieu d’exposition ou de culture : centre culturel, parc des expositions.");
    sProduit.createFeatureAttributeValue(nature26, "Parc de loisirs");
    FC_FeatureAttributeValue parcLoisirs26 = nature26
        .getFeatureAttributeValueByName("Parc de loisirs");
    parcLoisirs26
        .setDefinition("Parc à caractère commercial spécialement aménagé pour les loisirs : centre permanent de jeux, parc d’attraction, parc de détente, centre de loisirs. Seuls les parcs dont la superficie excède 10 ha et dotés d’équipements conséquents sont inclus. Les parcs publics (jardins, parcs communaux, départementaux…) sont exclus (voir la valeur « espace public » ci-dessus).");
    sProduit.createFeatureAttributeValue(nature26, "Parc zoologique");
    FC_FeatureAttributeValue zoo26 = nature26
        .getFeatureAttributeValueByName("Parc zoologique");
    zoo26
        .setDefinition("Parc ouvert au public, où il est possible de voir des animaux sauvages vivant en captivité ou en semi-liberté. Tous les parcs ouverts au public sont inclus.");
    sProduit.createFeatureAttributeValue(nature26, "Refuge");
    FC_FeatureAttributeValue refuge26 = nature26
        .getFeatureAttributeValueByName("Refuge");
    refuge26.setDefinition("Refuge, refuge gardé, abri de montagne nommé.");
    sProduit.createFeatureAttributeValue(nature26, "Vestiges archéologiques");
    FC_FeatureAttributeValue arch26 = nature26
        .getFeatureAttributeValueByName("Vestiges archéologiques");
    arch26
        .setDefinition("Vestiges archéologiques, fouilles, tumulus, oppidum.");
    sProduit.createFeatureAttributeValue(nature26, "Village de vacances");
    FC_FeatureAttributeValue village26 = nature26
        .getFeatureAttributeValueByName("Village de vacances");
    village26
        .setDefinition("Établissement de vacances, comprenant des équipements sportifs ou de détente conséquents dont le gestionnaire est privé ou public : village de vacances, colonie de vacances. Les hôtels et les « camps de vacances » sont exclus, ainsi que les établissements dont la capacité de prise en charge est inférieure à 300 personnes.");
    sProduit.createFeatureAttributeValue(nature26, "NR");
    FC_FeatureAttributeValue nr26 = nature26
        .getFeatureAttributeValueByName("NR");
    nr26.setDefinition("Non renseignée, l’information est manquante dans la base.");

    // Attribut TOPONYME
    sProduit
        .createFeatureAttribute(paiCultLoisirs, "TOPONYME", "string", false);
    AttributeType top26 = paiCultLoisirs.getFeatureAttributeByName("TOPONYME");
    top26
        .setDefinition("Nom validé par l’Equipe Produit Toponymie qui garantit son orthographe.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(paiCultLoisirs, "IMPORTANCE", "string",
        true);
    AttributeType importance26 = paiCultLoisirs
        .getFeatureAttributeByName("IMPORTANCE");
    importance26
        .setDefinition("Importance du toponyme dans son environnement.");
    sProduit.createFeatureAttributeValue(importance26, "1");
    sProduit.createFeatureAttributeValue(importance26, "2");
    sProduit.createFeatureAttributeValue(importance26, "3");
    sProduit.createFeatureAttributeValue(importance26, "4");
    sProduit.createFeatureAttributeValue(importance26, "5");
    sProduit.createFeatureAttributeValue(importance26, "6");
    sProduit.createFeatureAttributeValue(importance26, "7");
    sProduit.createFeatureAttributeValue(importance26, "8");
    sProduit.createFeatureAttributeValue(importance26, "NC");
    sProduit.createFeatureAttributeValue(importance26, "NR");

    // Classe
    // PAI_ESPACE_NATUREL///////////////////////////////////////////////////

    sProduit.createFeatureType("PAI_ESPACE_NATUREL");
    FeatureType paiEspNat = (FeatureType) (sProduit
        .getFeatureTypeByName("PAI_ESPACE_NATUREL"));
    paiEspNat
        .setDefinition("Désignation d’un lieu-dit non habité dont le nom se rapporte ni à un détail orographique ni à un détail hydrographique.");
    paiEspNat.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(paiEspNat, "ID", "string", false);
    AttributeType id27 = paiEspNat.getFeatureAttributeByName("ID");
    id27.setDefinition("Identifiant du PAI. Cet identifiant est unique. Il est stable d’une édition à l’autre.");

    // Attribut ORIGINE
    sProduit.createFeatureAttribute(paiEspNat, "ORIGINE", "string", true);
    AttributeType origine27 = paiEspNat.getFeatureAttributeByName("ORIGINE");
    origine27.setDefinition("Source de l'information.");
    sProduit.createFeatureAttributeValue(origine27, "Plan");
    sProduit.createFeatureAttributeValue(origine27, "Fichier");
    sProduit.createFeatureAttributeValue(origine27, "Terrain");
    sProduit.createFeatureAttributeValue(origine27, "Scan25");
    sProduit.createFeatureAttributeValue(origine27, "Orthophotographie");
    sProduit.createFeatureAttributeValue(origine27, "BDTopo");
    sProduit.createFeatureAttributeValue(origine27, "BDCarto");
    sProduit.createFeatureAttributeValue(origine27, "Géoroute");
    sProduit.createFeatureAttributeValue(origine27, "BDNyme");
    sProduit.createFeatureAttributeValue(origine27, "Calculé");
    sProduit.createFeatureAttributeValue(origine27, "BDParcellaire");
    sProduit.createFeatureAttributeValue(origine27, "NR");

    // Attribut NATURE
    sProduit.createFeatureAttribute(paiEspNat, "NATURE", "string", true);
    AttributeType nature27 = paiEspNat.getFeatureAttributeByName("NATURE");
    nature27.setDefinition("Nature du PAI.");
    sProduit.createFeatureAttributeValue(nature27, "Arbre");
    FC_FeatureAttributeValue arbre27 = nature27
        .getFeatureAttributeValueByName("Arbre");
    arbre27.setDefinition("Arbre nommé isolé, arbre remarquable.");
    sProduit.createFeatureAttributeValue(nature27, "Bois");
    FC_FeatureAttributeValue bois27 = nature27
        .getFeatureAttributeValueByName("Bois");
    bois27.setDefinition("Bois ou forêt.");
    sProduit.createFeatureAttributeValue(nature27, "Lieu-dit non habité");
    FC_FeatureAttributeValue lieu27 = nature27
        .getFeatureAttributeValueByName("Lieu-dit non habité");
    lieu27
        .setDefinition("Lieu-dit quelconque, dont le nom est généralement attaché à des terres : lieu-dit non habité, plantation, espace cultivé.");
    sProduit.createFeatureAttributeValue(nature27, "Parc");
    FC_FeatureAttributeValue parc27 = nature27
        .getFeatureAttributeValueByName("Parc");
    parc27
        .setDefinition("Espace réglementé, généralement libre d’accès pour le public et où la nature fait l’objet d’une protection spéciale : jardin, parc municipal, parc intercommunal, parc départemental, parc interdépartemental, parc naturel régional, parc national, réserve naturelle, parc marin. Les parcs à vocation commerciale ne sont pas pris en compte dans cet attribut.");
    sProduit.createFeatureAttributeValue(nature27, "Pare-feu");
    FC_FeatureAttributeValue pare27 = nature27
        .getFeatureAttributeValueByName("Pare-feu");
    pare27
        .setDefinition("Dispositif destiné à empêcher la propagation d’un incendie (généralement, ouverture pratiquée dans le massif forestier menacé).");
    sProduit.createFeatureAttributeValue(nature27, "Point de vue");
    FC_FeatureAttributeValue point27 = nature27
        .getFeatureAttributeValueByName("Point de vue");
    point27
        .setDefinition("Endroit d’où l’on jouit d’une vue pittoresque : point de vue, table d’orientation, belvédère. Seuls les points de vue aménagés (table d’orientation, bancs,…) sont inclus");
    sProduit.createFeatureAttributeValue(nature27, "NR");
    FC_FeatureAttributeValue nr27 = nature27
        .getFeatureAttributeValueByName("NR");
    nr27.setDefinition("Non renseignée, l’information est manquante dans la base.");

    // Attribut TOPONYME
    sProduit.createFeatureAttribute(paiEspNat, "TOPONYME", "string", false);
    AttributeType top27 = paiEspNat.getFeatureAttributeByName("TOPONYME");
    top27
        .setDefinition("Nom validé par l’Equipe Produit Toponymie qui garantit son orthographe.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(paiEspNat, "IMPORTANCE", "string", true);
    AttributeType importance27 = paiEspNat
        .getFeatureAttributeByName("IMPORTANCE");
    importance27
        .setDefinition("Importance du toponyme dans son environnement.");
    sProduit.createFeatureAttributeValue(importance27, "1");
    sProduit.createFeatureAttributeValue(importance27, "2");
    sProduit.createFeatureAttributeValue(importance27, "3");
    sProduit.createFeatureAttributeValue(importance27, "4");
    sProduit.createFeatureAttributeValue(importance27, "5");
    sProduit.createFeatureAttributeValue(importance27, "6");
    sProduit.createFeatureAttributeValue(importance27, "7");
    sProduit.createFeatureAttributeValue(importance27, "8");
    sProduit.createFeatureAttributeValue(importance27, "NC");
    sProduit.createFeatureAttributeValue(importance27, "NR");

    // Classe
    // PAI_SCIENCE_ENSEIGNEMENT///////////////////////////////////////////////////

    sProduit.createFeatureType("PAI_SCIENCE_ENSEIGNEMENT");
    FeatureType paiScience = (FeatureType) (sProduit
        .getFeatureTypeByName("PAI_SCIENCE_ENSEIGNEMENT"));
    paiScience
        .setDefinition("Désignation d’un établissement d’enseignement ou de recherche.");
    paiScience.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(paiScience, "ID", "string", false);
    AttributeType id28 = paiScience.getFeatureAttributeByName("ID");
    id28.setDefinition("Identifiant du PAI. Cet identifiant est unique. Il est stable d’une édition à l’autre.");

    // Attribut ORIGINE
    sProduit.createFeatureAttribute(paiScience, "ORIGINE", "string", true);
    AttributeType origine28 = paiScience.getFeatureAttributeByName("ORIGINE");
    origine28.setDefinition("Source de l'information.");
    sProduit.createFeatureAttributeValue(origine28, "Plan");
    sProduit.createFeatureAttributeValue(origine28, "Fichier");
    sProduit.createFeatureAttributeValue(origine28, "Terrain");
    sProduit.createFeatureAttributeValue(origine28, "Scan25");
    sProduit.createFeatureAttributeValue(origine28, "Orthophotographie");
    sProduit.createFeatureAttributeValue(origine28, "BDTopo");
    sProduit.createFeatureAttributeValue(origine28, "BDCarto");
    sProduit.createFeatureAttributeValue(origine28, "Géoroute");
    sProduit.createFeatureAttributeValue(origine28, "BDNyme");
    sProduit.createFeatureAttributeValue(origine28, "Calculé");
    sProduit.createFeatureAttributeValue(origine28, "BDParcellaire");
    sProduit.createFeatureAttributeValue(origine28, "NR");

    // Attribut NATURE
    sProduit.createFeatureAttribute(paiScience, "NATURE", "string", true);
    AttributeType nature28 = paiScience.getFeatureAttributeByName("NATURE");
    nature28.setDefinition("Nature du PAI.");
    sProduit.createFeatureAttributeValue(nature28, "Enseignement primaire");
    FC_FeatureAttributeValue prim28 = nature28
        .getFeatureAttributeValueByName("Enseignement primaire");
    prim28
        .setDefinition("Établissement consacré à l’enseignement maternel et primaire : école primaire, école maternelle, groupe scolaire, Institut Médico-Pédagogique (I.M.P.). Tous les établissements d’enseignement primaire, publics, confessionnels ou privés, ayant un contrat simple ou d’association avec l’État sont inclus. Les crèches sont exclues.");
    sProduit.createFeatureAttributeValue(nature28, "Enseignement secondaire");
    FC_FeatureAttributeValue sec28 = nature28
        .getFeatureAttributeValueByName("Enseignement secondaire");
    sec28
        .setDefinition("Établissement consacré à l’enseignement secondaire : collège, lycée, Centre d’Aide par le Travail (C.A.T.), Formation Professionnelle des Adultes (F.P.A.), Institut Médico-Professionnel (I.M.Pro.). Tous les établissements d’enseignement secondaire publics, confessionnels ou privés, ayant un contrat simple ou d’association avec l’État sont inclus.");
    sProduit.createFeatureAttributeValue(nature28, "Enseignement supérieur");
    FC_FeatureAttributeValue sup28 = nature28
        .getFeatureAttributeValueByName("Enseignement supérieur");
    sup28
        .setDefinition("Établissement consacré à l’enseignement supérieur : faculté, centre universitaire, institut, grande école,etc. Tous les établissements d’enseignement supérieur publics, confessionnels ou privés, ayant un contrat simple ou d’association avec l’État sont inclus. Les cours du soir, les cités et les restaurants universitaires sont exclus.");
    sProduit.createFeatureAttributeValue(nature28, "Science");
    FC_FeatureAttributeValue sc28 = nature28
        .getFeatureAttributeValueByName("Science");
    sc28.setDefinition("Etablissement scientifique ou technique nommé : centre de recherche, laboratoire, observatoire, station scientifique.");
    sProduit.createFeatureAttributeValue(nature28, "NR");
    FC_FeatureAttributeValue nr28 = nature28
        .getFeatureAttributeValueByName("NR");
    nr28.setDefinition("Non renseignée, l’information est manquante dans la base.");

    // Attribut TOPONYME
    sProduit.createFeatureAttribute(paiScience, "TOPONYME", "string", false);
    AttributeType top28 = paiScience.getFeatureAttributeByName("TOPONYME");
    top28
        .setDefinition("Nom validé par l’Equipe Produit Toponymie qui garantit son orthographe.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(paiScience, "IMPORTANCE", "string", true);
    AttributeType importance28 = paiScience
        .getFeatureAttributeByName("IMPORTANCE");
    importance28
        .setDefinition("Importance du toponyme dans son environnement.");
    sProduit.createFeatureAttributeValue(importance28, "1");
    sProduit.createFeatureAttributeValue(importance28, "2");
    sProduit.createFeatureAttributeValue(importance28, "3");
    sProduit.createFeatureAttributeValue(importance28, "4");
    sProduit.createFeatureAttributeValue(importance28, "5");
    sProduit.createFeatureAttributeValue(importance28, "6");
    sProduit.createFeatureAttributeValue(importance28, "7");
    sProduit.createFeatureAttributeValue(importance28, "8");
    sProduit.createFeatureAttributeValue(importance28, "NC");
    sProduit.createFeatureAttributeValue(importance28, "NR");

    // Classe
    // PAI_GESTION_DES_EAUX///////////////////////////////////////////////////

    sProduit.createFeatureType("PAI_GESTION_DES_EAUX");
    FeatureType paiGestEaux = (FeatureType) (sProduit
        .getFeatureTypeByName("PAI_GESTION_DES_EAUX"));
    paiGestEaux
        .setDefinition("Désignation d’une construction ou site liés à l’approvisionnement, au traitement de l’eau pour différents besoins (agricole, industriel, consommation) ou à l’épuration des eaux usées avant rejet dans la nature.");
    paiGestEaux.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(paiGestEaux, "ID", "string", false);
    AttributeType id29 = paiGestEaux.getFeatureAttributeByName("ID");
    id29.setDefinition("Identifiant du PAI. Cet identifiant est unique. Il est stable d’une édition à l’autre.");

    // Attribut ORIGINE
    sProduit.createFeatureAttribute(paiGestEaux, "ORIGINE", "string", true);
    AttributeType origine29 = paiGestEaux.getFeatureAttributeByName("ORIGINE");
    origine29.setDefinition("Source de l'information.");
    sProduit.createFeatureAttributeValue(origine29, "Plan");
    sProduit.createFeatureAttributeValue(origine29, "Fichier");
    sProduit.createFeatureAttributeValue(origine29, "Terrain");
    sProduit.createFeatureAttributeValue(origine29, "Scan25");
    sProduit.createFeatureAttributeValue(origine29, "Orthophotographie");
    sProduit.createFeatureAttributeValue(origine29, "BDTopo");
    sProduit.createFeatureAttributeValue(origine29, "BDCarto");
    sProduit.createFeatureAttributeValue(origine29, "Géoroute");
    sProduit.createFeatureAttributeValue(origine29, "BDNyme");
    sProduit.createFeatureAttributeValue(origine29, "Calculé");
    sProduit.createFeatureAttributeValue(origine29, "BDParcellaire");
    sProduit.createFeatureAttributeValue(origine29, "NR");

    // Attribut NATURE
    sProduit.createFeatureAttribute(paiGestEaux, "NATURE", "string", true);
    AttributeType nature29 = paiGestEaux.getFeatureAttributeByName("NATURE");
    nature29.setDefinition("Nature du PAI.");
    sProduit.createFeatureAttributeValue(nature29, "Station de pompage");
    FC_FeatureAttributeValue station29 = nature29
        .getFeatureAttributeValueByName("Station de pompage");
    station29
        .setDefinition("Site incluant au moins une construction abritant une installation de captage ou de pompage des eaux : captage, pompage pour besoins agricole ou industriel, pompage pour production eau potable. Toutes les stations de pompage servant à l’alimentation en eau potable d’une collectivité sont incluses.");
    sProduit.createFeatureAttributeValue(nature29,
        "Usine de traitement des eaux");
    FC_FeatureAttributeValue usine29 = nature29
        .getFeatureAttributeValueByName("Usine de traitement des eaux");
    usine29
        .setDefinition("Établissement comprenant des installations destinées à rendre l’eau propre à la consommation (usine de traitement des eaux) ou à épurer des eaux usées avant leur rejet dans la nature (stations d’épuration, de lagunage) : usine de traitement des eaux, station d’épuration, station de lagunage. Les stations d’épuration et de lagunage sont incluses. Les stations traitant l’eau afin de la rendre propre à la consommation sont incluses lorsqu’elles comprennent des installations conséquentes (usines comprenant bassins, filtrages, traitements mécaniques). Sont exclues les stations lorsqu’il s’agit uniquement d’un traitement chimique d’appoint effectué au niveau d’un captage ou d’un réservoir. Les stations de relèvement sont également exclues.");
    sProduit.createFeatureAttributeValue(nature29, "Enseignement supérieur");
    sProduit.createFeatureAttributeValue(nature29, "NR");
    FC_FeatureAttributeValue nr29 = nature29
        .getFeatureAttributeValueByName("NR");
    nr29.setDefinition("Non renseignée, l’information est manquante dans la base.");

    // Attribut TOPONYME
    sProduit.createFeatureAttribute(paiGestEaux, "TOPONYME", "string", false);
    AttributeType top29 = paiGestEaux.getFeatureAttributeByName("TOPONYME");
    top29
        .setDefinition("Nom validé par l’Equipe Produit Toponymie qui garantit son orthographe.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(paiGestEaux, "IMPORTANCE", "string", true);
    AttributeType importance29 = paiGestEaux
        .getFeatureAttributeByName("IMPORTANCE");
    importance29
        .setDefinition("Importance du toponyme dans son environnement.");
    sProduit.createFeatureAttributeValue(importance29, "1");
    sProduit.createFeatureAttributeValue(importance29, "2");
    sProduit.createFeatureAttributeValue(importance29, "3");
    sProduit.createFeatureAttributeValue(importance29, "4");
    sProduit.createFeatureAttributeValue(importance29, "5");
    sProduit.createFeatureAttributeValue(importance29, "6");
    sProduit.createFeatureAttributeValue(importance29, "7");
    sProduit.createFeatureAttributeValue(importance29, "8");
    sProduit.createFeatureAttributeValue(importance29, "NC");
    sProduit.createFeatureAttributeValue(importance29, "NR");

    // Classe
    // PAI_INDUSTRIEL_ET_COMMERCIAL///////////////////////////////////////////////////

    sProduit.createFeatureType("PAI_INDUSTRIEL_ET_COMMERCIAL");
    FeatureType paiIndusCom = (FeatureType) (sProduit
        .getFeatureTypeByName("PAI_INDUSTRIEL_ET_COMMERCIAL"));
    paiIndusCom
        .setDefinition("Désignation d’un bâtiment, site ou zone à caractère industriel ou commercial.");
    paiIndusCom.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(paiIndusCom, "ID", "string", false);
    AttributeType id30 = paiIndusCom.getFeatureAttributeByName("ID");
    id30.setDefinition("Identifiant du PAI. Cet identifiant est unique. Il est stable d’une édition à l’autre.");

    // Attribut ORIGINE
    sProduit.createFeatureAttribute(paiIndusCom, "ORIGINE", "string", true);
    AttributeType origine30 = paiIndusCom.getFeatureAttributeByName("ORIGINE");
    origine30.setDefinition("Source de l'information.");
    sProduit.createFeatureAttributeValue(origine30, "Plan");
    sProduit.createFeatureAttributeValue(origine30, "Fichier");
    sProduit.createFeatureAttributeValue(origine30, "Terrain");
    sProduit.createFeatureAttributeValue(origine30, "Scan25");
    sProduit.createFeatureAttributeValue(origine30, "Orthophotographie");
    sProduit.createFeatureAttributeValue(origine30, "BDTopo");
    sProduit.createFeatureAttributeValue(origine30, "BDCarto");
    sProduit.createFeatureAttributeValue(origine30, "Géoroute");
    sProduit.createFeatureAttributeValue(origine30, "BDNyme");
    sProduit.createFeatureAttributeValue(origine30, "Calculé");
    sProduit.createFeatureAttributeValue(origine30, "BDParcellaire");
    sProduit.createFeatureAttributeValue(origine30, "NR");

    // Attribut NATURE
    sProduit.createFeatureAttribute(paiIndusCom, "NATURE", "string", true);
    AttributeType nature30 = paiIndusCom.getFeatureAttributeByName("NATURE");
    nature30.setDefinition("Nature du PAI.");
    sProduit.createFeatureAttributeValue(nature30, "Aquaculture");
    FC_FeatureAttributeValue aqua30 = nature30
        .getFeatureAttributeValueByName("Aquaculture");
    aqua30
        .setDefinition("Site aménagé pour l’élevage piscicole ou la culture d’espèces animales marines (coques, coquilles Saint-Jacques, huîtres, moules, palourdes,…) : bouchot, parc à huîtres, zone conchylicole, zone mytilicole, zone ostréicole. Toutes les zones de plus de 3 ha possédant des installations fixes de pêche et délimitées par des alignements de pieux, les parcs à huîtres, les bassins. Les installations de pêche au carrelet sont exclues.");
    sProduit.createFeatureAttributeValue(nature30, "Carrière");
    FC_FeatureAttributeValue carriere30 = nature30
        .getFeatureAttributeValueByName("Carrière");
    carriere30
        .setDefinition("Lieu d’où l’on extrait à ciel ouvert des matériaux de construction (pierre, roche) : carrière, sablière, ballastière, gravière. Toutes les carrières de plus de 3 ha en exploitation sont incluses. La définition de l’enceinte s’appuie sur les fronts de taille (voir aussi la classe LIGNE_OROGRAPHIQUE) et sur la zone d’exploitation visible sur les photographies aériennes.");
    sProduit.createFeatureAttributeValue(nature30, "Centrale électrique");
    FC_FeatureAttributeValue centrale30 = nature30
        .getFeatureAttributeValueByName("Centrale électrique");
    centrale30
        .setDefinition("Usine où l’on produit de l’énergie électrique : centrale hydroélectrique, centrale thermique, centrale nucléaire. Les centrales électriques souterraines sont exclues.");
    sProduit.createFeatureAttributeValue(nature30, "Divers commercial");
    FC_FeatureAttributeValue divers30 = nature30
        .getFeatureAttributeValueByName("Divers commercial");
    divers30
        .setDefinition("Bâtiment ou zone à caractère commercial : hypermarché, grand magasin, centre commercial, zone à caractère commercial. Au moins tous les sites incluant un « grand magasin », un hypermarché, ou une zone d’activité commerciale d’au moins 5 ha. Les hypermarchés isolés ayant une surface de vente de plus de 4000m2 sont inclus. (voir également la valeur d’attribut « Marché » ci-dessous).");
    sProduit.createFeatureAttributeValue(nature30, "Divers industriel");
    FC_FeatureAttributeValue indus30 = nature30
        .getFeatureAttributeValueByName("Divers industriel");
    indus30
        .setDefinition("Organisme ou entreprise à caractère industriel non distingué de façon spécifique : centre de recherche, dépôt, coopérative (vinicole, céréalière…), champignonnière, élevage avicole, haras, abattoir, déchèterie. Tous les sites d’importance ou de notoriété nationale ou régionale, confirmée par un toponyme, et de surface supérieure à 3 ha sont retenus (le toponyme n’est pas nécessairement retenu).");
    sProduit.createFeatureAttributeValue(nature30, "Haras national");
    FC_FeatureAttributeValue haras30 = nature30
        .getFeatureAttributeValueByName("Haras national");
    haras30
        .setDefinition("Lieu ou établissement destiné à la reproduction de l’espèce chevaline, à l’amélioration des races de chevaux par la sélection des étalons. Tous les haras nationaux sont inclus. L’enceinte comprend l’ensemble des installations (manège, écuries, piste d’entraînement,…).");
    sProduit.createFeatureAttributeValue(nature30, "Marais salants");
    FC_FeatureAttributeValue marais30 = nature30
        .getFeatureAttributeValueByName("Marais salants");
    marais30
        .setDefinition("Zone constituée de bassins creusés à proximité des côtes pour extraire le sel de l’eau de mer par évaporation. Les zones de marais salants de moins de 3 ha sont exclues. Les anciens marais salants qui ne sont plus en activité sont exclus.");
    sProduit.createFeatureAttributeValue(nature30, "Marché");
    FC_FeatureAttributeValue marche30 = nature30
        .getFeatureAttributeValueByName("Marché");
    marche30
        .setDefinition("Tout ensemble construit dont la finalité est la commercialisation de gros ou de détail de denrées alimentaires : marché couvert, marché d’intérêt national, marché d’intérêt régional, halle, foire, zone d’exposition à caractère permanent, criée couverte.");
    sProduit.createFeatureAttributeValue(nature30, "Mine");
    FC_FeatureAttributeValue mine30 = nature30
        .getFeatureAttributeValueByName("Mine");
    mine30
        .setDefinition("Lieu d’où l’on extrait des minerais : mine de houille, mine de lignite, crassier, entrée de mine, terril. Les mines à ciel ouvert de plus de 10 ha sont incluses. Les mines souterraines sont exclues.");
    sProduit.createFeatureAttributeValue(nature30, "Usine");
    FC_FeatureAttributeValue usine30 = nature30
        .getFeatureAttributeValueByName("Usine");
    usine30
        .setDefinition("Établissement dominé par une activité industrielle (fabrication d’objets ou de produits, transformation ou conservation de matières premières) : atelier, fabrique, manufacture, mine avec infrastructure bâtie, usine, scierie. Les sites dont la superficie est inférieure à 5 ha sont généralement exclus.");
    sProduit.createFeatureAttributeValue(nature30, "Zone industrielle");
    FC_FeatureAttributeValue zone30 = nature30
        .getFeatureAttributeValueByName("Zone industrielle");
    zone30
        .setDefinition("Regroupement d'activités de production sur l’initiative des collectivités locales ou d'organismes parapublics (chambres de commerce et d'industrie) et portant un nom : zone artisanale, zone industrielle. Les sites dont la superficie est inférieure à 5 ha sont généralement exclus.");
    sProduit.createFeatureAttributeValue(nature30, "NR");
    FC_FeatureAttributeValue nr30 = nature30
        .getFeatureAttributeValueByName("NR");
    nr30.setDefinition("Non renseignée, l’information est manquante dans la base.");

    // Attribut TOPONYME
    sProduit.createFeatureAttribute(paiIndusCom, "TOPONYME", "string", false);
    AttributeType top30 = paiIndusCom.getFeatureAttributeByName("TOPONYME");
    top30
        .setDefinition("Nom validé par l’Equipe Produit Toponymie qui garantit son orthographe.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(paiIndusCom, "IMPORTANCE", "string", true);
    AttributeType importance30 = paiIndusCom
        .getFeatureAttributeByName("IMPORTANCE");
    importance30
        .setDefinition("Importance du toponyme dans son environnement.");
    sProduit.createFeatureAttributeValue(importance30, "1");
    sProduit.createFeatureAttributeValue(importance30, "2");
    sProduit.createFeatureAttributeValue(importance30, "3");
    sProduit.createFeatureAttributeValue(importance30, "4");
    sProduit.createFeatureAttributeValue(importance30, "5");
    sProduit.createFeatureAttributeValue(importance30, "6");
    sProduit.createFeatureAttributeValue(importance30, "7");
    sProduit.createFeatureAttributeValue(importance30, "8");
    sProduit.createFeatureAttributeValue(importance30, "NC");
    sProduit.createFeatureAttributeValue(importance30, "NR");

    // Classe
    // PAI_RELIGIEUX///////////////////////////////////////////////////

    sProduit.createFeatureType("PAI_RELIGIEUX");
    FeatureType paiReligieux = (FeatureType) (sProduit
        .getFeatureTypeByName("PAI_RELIGIEUX"));
    paiReligieux
        .setDefinition("Désignation d’un bâtiment réservé à la pratique d’une religion.");
    paiReligieux.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(paiReligieux, "ID", "string", false);
    AttributeType id31 = paiReligieux.getFeatureAttributeByName("ID");
    id31.setDefinition("Identifiant du PAI. Cet identifiant est unique. Il est stable d’une édition à l’autre.");

    // Attribut ORIGINE
    sProduit.createFeatureAttribute(paiReligieux, "ORIGINE", "string", true);
    AttributeType origine31 = paiReligieux.getFeatureAttributeByName("ORIGINE");
    origine31.setDefinition("Source de l'information.");
    sProduit.createFeatureAttributeValue(origine31, "Plan");
    sProduit.createFeatureAttributeValue(origine31, "Fichier");
    sProduit.createFeatureAttributeValue(origine31, "Terrain");
    sProduit.createFeatureAttributeValue(origine31, "Scan25");
    sProduit.createFeatureAttributeValue(origine31, "Orthophotographie");
    sProduit.createFeatureAttributeValue(origine31, "BDTopo");
    sProduit.createFeatureAttributeValue(origine31, "BDCarto");
    sProduit.createFeatureAttributeValue(origine31, "Géoroute");
    sProduit.createFeatureAttributeValue(origine31, "BDNyme");
    sProduit.createFeatureAttributeValue(origine31, "Calculé");
    sProduit.createFeatureAttributeValue(origine31, "BDParcellaire");
    sProduit.createFeatureAttributeValue(origine31, "NR");

    // Attribut NATURE
    sProduit.createFeatureAttribute(paiReligieux, "NATURE", "string", true);
    AttributeType nature31 = paiReligieux.getFeatureAttributeByName("NATURE");
    nature31.setDefinition("Nature du PAI.");
    sProduit.createFeatureAttributeValue(nature31, "Croix");
    FC_FeatureAttributeValue croix31 = nature31
        .getFeatureAttributeValueByName("Croix");
    croix31
        .setDefinition("Monument religieux : croix, calvaire, vierge, statue religieuse.");
    sProduit.createFeatureAttributeValue(nature31,
        "Culte catholique ou orthodoxe");
    FC_FeatureAttributeValue catho31 = nature31
        .getFeatureAttributeValueByName("Culte catholique ou orthodoxe");
    catho31
        .setDefinition("Bâtiment réservé à l’exercice du culte catholique ou orthodoxe : église, cathédrale, basilique, chapelle, abbaye, oratoire.");
    sProduit.createFeatureAttributeValue(nature31, "Culte protestant");
    FC_FeatureAttributeValue prot31 = nature31
        .getFeatureAttributeValueByName("Culte protestant");
    prot31
        .setDefinition("Bâtiment réservé à l’exercice du culte protestant : temple (protestant), église réformée.");
    sProduit.createFeatureAttributeValue(nature31, "Culte israélite");
    FC_FeatureAttributeValue isra31 = nature31
        .getFeatureAttributeValueByName("Culte israélite");
    isra31
        .setDefinition("Bâtiment réservé à l’exercice du culte israélite : synagogue.");
    sProduit.createFeatureAttributeValue(nature31, "Culte islamique");
    FC_FeatureAttributeValue isla31 = nature31
        .getFeatureAttributeValueByName("Culte islamique");
    isla31
        .setDefinition("Bâtiment réservé à l’exercice du culte islamique : mosquée.");
    sProduit.createFeatureAttributeValue(nature31, "Culte divers");
    FC_FeatureAttributeValue divers31 = nature31
        .getFeatureAttributeValueByName("Culte divers");
    divers31
        .setDefinition("Bâtiment réservé à l’exercice d’un culte religieux autre que chrétien, islamique ou israélite : temple bouddhiste, temple hindouiste.");
    sProduit.createFeatureAttributeValue(nature31, "Tombeau");
    FC_FeatureAttributeValue tombeau31 = nature31
        .getFeatureAttributeValueByName("Tombeau");
    tombeau31
        .setDefinition("Cimetière, tombe ou tombeau nommé : cimetière, tombe, tombeau, ossuaire, funérarium.");
    sProduit.createFeatureAttributeValue(nature31, "NR");
    FC_FeatureAttributeValue nr31 = nature31
        .getFeatureAttributeValueByName("NR");
    nr31.setDefinition("Non renseignée, l’information est manquante dans la base.");

    // Attribut TOPONYME
    sProduit.createFeatureAttribute(paiReligieux, "TOPONYME", "string", false);
    AttributeType top31 = paiReligieux.getFeatureAttributeByName("TOPONYME");
    top31
        .setDefinition("Nom validé par l’Equipe Produit Toponymie qui garantit son orthographe.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(paiReligieux, "IMPORTANCE", "string", true);
    AttributeType importance31 = paiReligieux
        .getFeatureAttributeByName("IMPORTANCE");
    importance31
        .setDefinition("Importance du toponyme dans son environnement.");
    sProduit.createFeatureAttributeValue(importance31, "1");
    sProduit.createFeatureAttributeValue(importance31, "2");
    sProduit.createFeatureAttributeValue(importance31, "3");
    sProduit.createFeatureAttributeValue(importance31, "4");
    sProduit.createFeatureAttributeValue(importance31, "5");
    sProduit.createFeatureAttributeValue(importance31, "6");
    sProduit.createFeatureAttributeValue(importance31, "7");
    sProduit.createFeatureAttributeValue(importance31, "8");
    sProduit.createFeatureAttributeValue(importance31, "NC");
    sProduit.createFeatureAttributeValue(importance31, "NR");

    // Classe PAI_SANTE///////////////////////////////////////////////////

    sProduit.createFeatureType("PAI_SANTE");
    FeatureType paiSante = (FeatureType) (sProduit
        .getFeatureTypeByName("PAI_SANTE"));
    paiSante
        .setDefinition("Désignation d’un établissement thermal ou de type hospitalier.");
    paiSante.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(paiSante, "ID", "string", false);
    AttributeType id32 = paiSante.getFeatureAttributeByName("ID");
    id32.setDefinition("Identifiant du PAI. Cet identifiant est unique. Il est stable d’une édition à l’autre.");

    // Attribut ORIGINE
    sProduit.createFeatureAttribute(paiSante, "ORIGINE", "string", true);
    AttributeType origine32 = paiSante.getFeatureAttributeByName("ORIGINE");
    origine32.setDefinition("Source de l'information.");
    sProduit.createFeatureAttributeValue(origine32, "Plan");
    sProduit.createFeatureAttributeValue(origine32, "Fichier");
    sProduit.createFeatureAttributeValue(origine32, "Terrain");
    sProduit.createFeatureAttributeValue(origine32, "Scan25");
    sProduit.createFeatureAttributeValue(origine32, "Orthophotographie");
    sProduit.createFeatureAttributeValue(origine32, "BDTopo");
    sProduit.createFeatureAttributeValue(origine32, "BDCarto");
    sProduit.createFeatureAttributeValue(origine32, "Géoroute");
    sProduit.createFeatureAttributeValue(origine32, "BDNyme");
    sProduit.createFeatureAttributeValue(origine32, "Calculé");
    sProduit.createFeatureAttributeValue(origine32, "BDParcellaire");
    sProduit.createFeatureAttributeValue(origine32, "NR");

    // Attribut NATURE
    sProduit.createFeatureAttribute(paiSante, "NATURE", "string", true);
    AttributeType nature32 = paiSante.getFeatureAttributeByName("NATURE");
    nature32.setDefinition("Nature du PAI.");
    sProduit.createFeatureAttributeValue(nature32, "Etablissement hospitalier");
    FC_FeatureAttributeValue hop32 = nature32
        .getFeatureAttributeValueByName("Etablissement hospitalier");
    hop32
        .setDefinition("Établissement public ou privé qui reçoit ou traite pendant un temps limité les malades, les blessés et les femmes en couches : hôpital, sanatorium, hospice, centre de soins, dispensaire, hôpital de jour, hôpital psychiatrique, etc. Tous les établissements assurant les soins et l’hébergement ou les soins seulement sont inclus. Les maisons de retraite ne possédant pas de centre de soins sont exclues.");
    sProduit.createFeatureAttributeValue(nature32, "Etablissement thermal");
    FC_FeatureAttributeValue therm32 = nature32
        .getFeatureAttributeValueByName("Etablissement thermal");
    therm32
        .setDefinition("Établissement où l’on utilise les eaux médicinales (eaux minérales, chaudes ou non) : établissement thermal, centre de thalassothérapie. Seuls sont inclus les établissements agréés par la Sécurité Sociale.");
    sProduit.createFeatureAttributeValue(nature32, "Hôpital");
    FC_FeatureAttributeValue hopBis32 = nature32
        .getFeatureAttributeValueByName("Hôpital");
    hopBis32
        .setDefinition("Établissement public ou privé, où sont effectués tous les soins médicaux et chirurgicaux lourds et/ou de longue durée, ainsi que les accouchements : hôpital, CHU, hôpital militaire, clinique.");
    sProduit.createFeatureAttributeValue(nature32, "NR");
    FC_FeatureAttributeValue nr32 = nature32
        .getFeatureAttributeValueByName("NR");
    nr32.setDefinition("Non renseignée, l’information est manquante dans la base.");

    // Attribut TOPONYME
    sProduit.createFeatureAttribute(paiSante, "TOPONYME", "string", false);
    AttributeType top32 = paiSante.getFeatureAttributeByName("TOPONYME");
    top32
        .setDefinition("Nom validé par l’Equipe Produit Toponymie qui garantit son orthographe.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(paiSante, "IMPORTANCE", "string", true);
    AttributeType importance32 = paiSante
        .getFeatureAttributeByName("IMPORTANCE");
    importance32
        .setDefinition("Importance du toponyme dans son environnement.");
    sProduit.createFeatureAttributeValue(importance32, "1");
    sProduit.createFeatureAttributeValue(importance32, "2");
    sProduit.createFeatureAttributeValue(importance32, "3");
    sProduit.createFeatureAttributeValue(importance32, "4");
    sProduit.createFeatureAttributeValue(importance32, "5");
    sProduit.createFeatureAttributeValue(importance32, "6");
    sProduit.createFeatureAttributeValue(importance32, "7");
    sProduit.createFeatureAttributeValue(importance32, "8");
    sProduit.createFeatureAttributeValue(importance32, "NC");
    sProduit.createFeatureAttributeValue(importance32, "NR");

    // Classe PAI_SPORT///////////////////////////////////////////////////

    sProduit.createFeatureType("PAI_SPORT");
    FeatureType paiSport = (FeatureType) (sProduit
        .getFeatureTypeByName("PAI_SPORT"));
    paiSport
        .setDefinition("Désignation d’un établissement ou lieu spécialement aménagé pour la pratique d’une ou de plusieurs activités sportives.");
    paiSport.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(paiSport, "ID", "string", false);
    AttributeType id33 = paiSport.getFeatureAttributeByName("ID");
    id33.setDefinition("Identifiant du PAI. Cet identifiant est unique. Il est stable d’une édition à l’autre.");

    // Attribut ORIGINE
    sProduit.createFeatureAttribute(paiSport, "ORIGINE", "string", true);
    AttributeType origine33 = paiSport.getFeatureAttributeByName("ORIGINE");
    origine33.setDefinition("Source de l'information.");
    sProduit.createFeatureAttributeValue(origine33, "Plan");
    sProduit.createFeatureAttributeValue(origine33, "Fichier");
    sProduit.createFeatureAttributeValue(origine33, "Terrain");
    sProduit.createFeatureAttributeValue(origine33, "Scan25");
    sProduit.createFeatureAttributeValue(origine33, "Orthophotographie");
    sProduit.createFeatureAttributeValue(origine33, "BDTopo");
    sProduit.createFeatureAttributeValue(origine33, "BDCarto");
    sProduit.createFeatureAttributeValue(origine33, "Géoroute");
    sProduit.createFeatureAttributeValue(origine33, "BDNyme");
    sProduit.createFeatureAttributeValue(origine33, "Calculé");
    sProduit.createFeatureAttributeValue(origine33, "BDParcellaire");
    sProduit.createFeatureAttributeValue(origine33, "NR");

    // Attribut NATURE
    sProduit.createFeatureAttribute(paiSport, "NATURE", "string", true);
    AttributeType nature33 = paiSport.getFeatureAttributeByName("NATURE");
    nature33.setDefinition("Nature du PAI.");
    sProduit.createFeatureAttributeValue(nature33, "Golf");
    FC_FeatureAttributeValue golf33 = nature33
        .getFeatureAttributeValueByName("Golf");
    golf33
        .setDefinition("Terrain ouvert au public et consacré à la pratique du golf. Les terrains de moins de 9 trous et les minigolfs sont exclus.");
    sProduit.createFeatureAttributeValue(nature33, "Hippodrome");
    FC_FeatureAttributeValue hip33 = nature33
        .getFeatureAttributeValueByName("Hippodrome");
    hip33
        .setDefinition("Lieu ouvert au public et consacré aux courses de chevaux. Seuls les hippodromes possédant des aménagements conséquents (tribunes, bâtiments spécifiques) sont inclus.");
    sProduit.createFeatureAttributeValue(nature33, "Piscine");
    FC_FeatureAttributeValue piscine33 = nature33
        .getFeatureAttributeValueByName("Piscine");
    piscine33
        .setDefinition("Grand bassin de natation, et ensemble des installations qui l'entourent : piscine couverte, piscine découverte. Toutes les piscines ouvertes au public et ayant un bassin au moins de 25 m ou plus sont incluses. Les piscines des centres de vacances ou des hôtels sont exclues (voir la classe TERRAIN_SPORT).");
    sProduit.createFeatureAttributeValue(nature33, "Stade");
    FC_FeatureAttributeValue stade33 = nature33
        .getFeatureAttributeValueByName("Stade");
    stade33
        .setDefinition("Grande enceinte, terrain aménagé pour la pratique des sports, et le plus souvent entouré de gradins, de tribunes : stade, terrain de sports, vélodrome découvert, circuit auto-moto, complexe sportif pluridisciplinaire. Seules les enceintes incluant des aménagements conséquents (piste « construite », tribunes,…) sont incluses. Les terrains d’entraînement incluant seulement 2 ou 3 terrains de football et de petits vestiaires sont exclus (voir aussi la classe TERRAIN_SPORT)");
    sProduit.createFeatureAttributeValue(nature33, "NR");
    FC_FeatureAttributeValue nr33 = nature33
        .getFeatureAttributeValueByName("NR");
    nr33.setDefinition("Non renseignée, l’information est manquante dans la base.");

    // Attribut TOPONYME
    sProduit.createFeatureAttribute(paiSport, "TOPONYME", "string", false);
    AttributeType top33 = paiSport.getFeatureAttributeByName("TOPONYME");
    top33
        .setDefinition("Nom validé par l’Equipe Produit Toponymie qui garantit son orthographe.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(paiSport, "IMPORTANCE", "string", true);
    AttributeType importance33 = paiSport
        .getFeatureAttributeByName("IMPORTANCE");
    importance33
        .setDefinition("Importance du toponyme dans son environnement.");
    sProduit.createFeatureAttributeValue(importance33, "1");
    sProduit.createFeatureAttributeValue(importance33, "2");
    sProduit.createFeatureAttributeValue(importance33, "3");
    sProduit.createFeatureAttributeValue(importance33, "4");
    sProduit.createFeatureAttributeValue(importance33, "5");
    sProduit.createFeatureAttributeValue(importance33, "6");
    sProduit.createFeatureAttributeValue(importance33, "7");
    sProduit.createFeatureAttributeValue(importance33, "8");
    sProduit.createFeatureAttributeValue(importance33, "NC");
    sProduit.createFeatureAttributeValue(importance33, "NR");

    // Classe
    // PAI_TRANSPORT///////////////////////////////////////////////////

    sProduit.createFeatureType("PAI_TRANSPORT");
    FeatureType paiTransport = (FeatureType) (sProduit
        .getFeatureTypeByName("PAI_TRANSPORT"));
    paiTransport
        .setDefinition("Désignation d’un bâtiment ou site lié à une activité de transport routier, ferré ou aérien.");
    paiTransport.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(paiTransport, "ID", "string", false);
    AttributeType id34 = paiTransport.getFeatureAttributeByName("ID");
    id34.setDefinition("Identifiant du PAI. Cet identifiant est unique. Il est stable d’une édition à l’autre.");

    // Attribut ORIGINE
    sProduit.createFeatureAttribute(paiTransport, "ORIGINE", "string", true);
    AttributeType origine34 = paiTransport.getFeatureAttributeByName("ORIGINE");
    origine34.setDefinition("Source de l'information.");
    sProduit.createFeatureAttributeValue(origine34, "Plan");
    sProduit.createFeatureAttributeValue(origine34, "Fichier");
    sProduit.createFeatureAttributeValue(origine34, "Terrain");
    sProduit.createFeatureAttributeValue(origine34, "Scan25");
    sProduit.createFeatureAttributeValue(origine34, "Orthophotographie");
    sProduit.createFeatureAttributeValue(origine34, "BDTopo");
    sProduit.createFeatureAttributeValue(origine34, "BDCarto");
    sProduit.createFeatureAttributeValue(origine34, "Géoroute");
    sProduit.createFeatureAttributeValue(origine34, "BDNyme");
    sProduit.createFeatureAttributeValue(origine34, "Calculé");
    sProduit.createFeatureAttributeValue(origine34, "BDParcellaire");
    sProduit.createFeatureAttributeValue(origine34, "NR");

    // Attribut NATURE
    sProduit.createFeatureAttribute(paiTransport, "NATURE", "string", true);
    AttributeType nature34 = paiTransport.getFeatureAttributeByName("NATURE");
    nature34.setDefinition("Nature du PAI.");
    sProduit.createFeatureAttributeValue(nature34, "Aérodrome militaire");
    FC_FeatureAttributeValue aeroMili34 = nature34
        .getFeatureAttributeValueByName("Aérodrome militaire");
    aeroMili34
        .setDefinition("Tout terrain ou plan d'eau réservés à l’armée spécialement aménagé pour l'atterrissage, le décollage et les manoeuvres des aéronefs y compris les installations annexes qu'il peut comporter pour les besoins du trafic et le service des aéronefs : aérodrome militaire, héliport militaire.");
    sProduit.createFeatureAttributeValue(nature34, "Aérodrome non militaire");
    FC_FeatureAttributeValue aeroNonMili34 = nature34
        .getFeatureAttributeValueByName("Aérodrome non militaire");
    aeroNonMili34
        .setDefinition("Tout terrain ou plan d'eau spécialement aménagé pour l'atterrissage, le décollage et les manoeuvres des aéronefs y compris les installations annexes qu'il peut comporter pour les besoins du trafic et le service des aéronefs : altiport, aérodrome non militaire, héliport. Ne sont pas pris en compte les aéro-clubs, les terrains de vol à voile, les pistes d’ULM.");
    sProduit.createFeatureAttributeValue(nature34, "Aéroport international");
    FC_FeatureAttributeValue aeroInter34 = nature34
        .getFeatureAttributeValueByName("Aéroport international");
    aeroInter34
        .setDefinition("Aérodrome de statut international sur lequel ont été prévues des installations en vue de l'abri, de l'entretien ou de la répartition des aéronefs, ainsi que pour la réception, l'embarquement et le débarquement des passagers, le chargement et le déchargement des marchandises.");
    sProduit.createFeatureAttributeValue(nature34, "Aéroport quelconque");
    FC_FeatureAttributeValue aeroQuel34 = nature34
        .getFeatureAttributeValueByName("Aéroport quelconque");
    aeroQuel34
        .setDefinition("Aérodrome sur lequel ont été prévues des installations en vue de l'abri, de l'entretien ou de la répartition des aéronefs, ainsi que pour la réception, l'embarquement et le débarquement des passagers, le chargement et le déchargement des marchandises.");
    sProduit.createFeatureAttributeValue(nature34, "Aire de service");
    FC_FeatureAttributeValue aireService34 = nature34
        .getFeatureAttributeValueByName("Aire de service");
    aireService34
        .setDefinition("Espace aménagé à l’écart des chaussées, notamment des autoroutes, pour permettre aux usagers de se ravitailler en carburant. Emprise de l’aire. Les contours de la surface ne s’appuient jamais sur des tronçons de route (qui représentent l’axe des chaussées).");
    sProduit.createFeatureAttributeValue(nature34, "Aire de repos");
    FC_FeatureAttributeValue aireRepos34 = nature34
        .getFeatureAttributeValueByName("Aire de repos");
    aireRepos34
        .setDefinition("Espace aménagé (présence d’un point d’eau obligatoire) à l ‘écart des chaussées, notamment des autoroutes, pour permettre aux usagers de s’arrêter et de se reposer.Emprise de l’aire. Les contours de la surface ne s’appuient jamais sur des sur des tronçons de route qui représentent l’axe des chaussées.");
    sProduit.createFeatureAttributeValue(nature34, "Barrage");
    FC_FeatureAttributeValue barrage34 = nature34
        .getFeatureAttributeValueByName("Barrage");
    barrage34
        .setDefinition("Obstacle artificiel placé en travers d’un cours d’eau : barrage, écluse, vanne.");
    sProduit.createFeatureAttributeValue(nature34, "Carrefour");
    FC_FeatureAttributeValue carrefour34 = nature34
        .getFeatureAttributeValueByName("Carrefour");
    carrefour34
        .setDefinition("Noeud du réseau routier : carrefour routier, échangeur, rond-point.");
    sProduit.createFeatureAttributeValue(nature34, "Chemin");
    FC_FeatureAttributeValue chemin34 = nature34
        .getFeatureAttributeValueByName("Chemin");
    chemin34
        .setDefinition("Voie de communication non routière : allée, chemin, laie forestière, sentier.");
    sProduit.createFeatureAttributeValue(nature34, "Echangeur");
    FC_FeatureAttributeValue echangeur34 = nature34
        .getFeatureAttributeValueByName("Echangeur");
    echangeur34
        .setDefinition("Noeud du réseau routier à chaussées séparées. Echangeur autoroutier portant un nom.");
    sProduit.createFeatureAttributeValue(nature34, "Gare routière");
    FC_FeatureAttributeValue gareRout34 = nature34
        .getFeatureAttributeValueByName("Gare routière");
    gareRout34
        .setDefinition("Ensemble des installations destinées à l’embarquement et au débarquement de voyageurs en car ou en bus en un point déterminé. Ne sont pas retenues les gares routières des bus de ville, des bus scolaires, de la RATP et les dépôts de bus.");
    sProduit.createFeatureAttributeValue(nature34, "Gare voyageurs uniquement");
    FC_FeatureAttributeValue gareVoy34 = nature34
        .getFeatureAttributeValueByName("Gare voyageurs uniquement");
    gareVoy34
        .setDefinition("Établissement ferroviaire ou de transport par câble assurant avec ou sans personnel un service commercial de voyageurs : gare, station, point d’arrêt, station réseau ferré urbain, gare téléphérique. Toutes les gares et arrêts ferroviaires en service sont inclus.");
    sProduit.createFeatureAttributeValue(nature34, "Gare voyageurs et fret");
    FC_FeatureAttributeValue gareVoyFret34 = nature34
        .getFeatureAttributeValueByName("Gare voyageurs et fret");
    gareVoyFret34
        .setDefinition("Établissement ferroviaire assurant un service commercial de voyageurs et de marchandises.(Uniquement le bâtiment principal ouvert au public.)");
    sProduit.createFeatureAttributeValue(nature34, "Gare fret uniquement");
    FC_FeatureAttributeValue gareFret34 = nature34
        .getFeatureAttributeValueByName("Gare fret uniquement");
    gareFret34
        .setDefinition("Établissement ferroviaire assurant un service commercial de marchandises : gare de fret, point de desserte. Le fret aérien ou maritime est exclu.");
    sProduit.createFeatureAttributeValue(nature34, "Infrastructure routière");
    FC_FeatureAttributeValue infraRout34 = nature34
        .getFeatureAttributeValueByName("Infrastructure routière");
    infraRout34
        .setDefinition("Élément du réseau routier ne figurant ni en attribut d’un objet « tronçon de route » ni avec la valeur d’attribut « carrefour » : route, aire de péage, aire de repos.");
    sProduit.createFeatureAttributeValue(nature34, "Parking");
    FC_FeatureAttributeValue parking34 = nature34
        .getFeatureAttributeValueByName("Parking");
    parking34
        .setDefinition("Une aire de stationnement ou parking est une zone aménagée pour le stationnement des véhicules : aire de stationnement, parking, parking souterrain, parking à étages. Tous les parkings publics nommés de plus de 25 places sont inclus qu’ils soient souterrains ou aériens (ex. parkings municipaux), Les parkings de plus de 25 places associés à des services de transport (gares, aéroports) sont retenus même s’ils n’ont pas de nom propre. Les parkings d’aires de repos ou de service ne sont pas retenus (voir les valeur <Aire de repos> et <Aire de service>). Le parkings appartenant à des établissements purement commerciaux (ex. parking de supermarché) sont exclus (pour ces derniers, voir aussi la classe Surface de route). Un Parking est un objet ponctuel situé au centre de l’aire de stationnement, ou à l’entrée pour les parkings souterrains. Il est généralement associé à une surface pour des parkings aériens de plus de 5 ha.");
    sProduit.createFeatureAttributeValue(nature34, "Péage");
    FC_FeatureAttributeValue peage34 = nature34
        .getFeatureAttributeValueByName("Péage");
    peage34
        .setDefinition("Barrière de péage. Toutes les barrières de péage sont représentées, qu’elles soient ou non accompagnées d’un élargissement de la chaussée ou d’un bâtiment : péage d’autoroute, de pont, de route. Si aucun objet de la base n’est associé au péage (ni surface de route ni bâtiment), le point d’activité est saisi sur l’axe de la route au niveau de la barrière de péage. Le péage est modélisé par une surface incluant tous les objets associés à cette fonction : SURFACE_ROUTE BATI_REMARQUABLE ou les deux.");
    sProduit.createFeatureAttributeValue(nature34, "Pont");
    FC_FeatureAttributeValue pont34 = nature34
        .getFeatureAttributeValueByName("Pont");
    pont34
        .setDefinition("Ouvrage d'art permettant le franchissement d'une vallée ou d'une voie de communication : pont, passerelle, viaduc, gué, pont mobile.");
    sProduit.createFeatureAttributeValue(nature34, "Port");
    FC_FeatureAttributeValue port34 = nature34
        .getFeatureAttributeValueByName("Port");
    port34
        .setDefinition("Abri naturel ou artificiel aménagé pour recevoir les navires, pour l'embarquement et le débarquement de leur chargement : port de plaisance, port de pêche, port national, port privé, port international, port militaire.");
    sProduit.createFeatureAttributeValue(nature34, "Rond-point");
    FC_FeatureAttributeValue rondP34 = nature34
        .getFeatureAttributeValueByName("Rond-point");
    rondP34
        .setDefinition("Rond-point, place de forme circulaire, ovale ou semi-circulaire, ou carrefour giratoire. Un giratoire est formé d'un anneau central qui permet aux usagers de prendre n'importe quelle direction, y compris de faire un demi-tour. Seuls les ronds-points nommés sont retenus.");
    sProduit.createFeatureAttributeValue(nature34, "Station de métro");
    FC_FeatureAttributeValue stationM34 = nature34
        .getFeatureAttributeValueByName("Station de métro");
    stationM34
        .setDefinition("Station où il est possible d'accéder au réseau du métro. On saisit un seul objet 'Station de métro' même s'il y a plusieurs entrées distinctes, éventuellement plusieurs ponctuels 'Station de métro' pour les correspondances importantes (ex : Bastille) mais un seul ponctuel pour une station de métro qui n’est pas une correspondance.");
    sProduit.createFeatureAttributeValue(nature34, "Téléphérique");
    FC_FeatureAttributeValue telepherique34 = nature34
        .getFeatureAttributeValueByName("Téléphérique");
    telepherique34
        .setDefinition("Remonte-pente, télécabine, télésiège, téléphérique, téléski.");
    sProduit.createFeatureAttributeValue(nature34, "Tunnel");
    FC_FeatureAttributeValue tunnel34 = nature34
        .getFeatureAttributeValueByName("Tunnel");
    tunnel34.setDefinition("Tunnel.");
    sProduit.createFeatureAttributeValue(nature34, "Voie ferrée");
    FC_FeatureAttributeValue voieF34 = nature34
        .getFeatureAttributeValueByName("Voie ferrée");
    voieF34.setDefinition("Voie ferrée nommée.");
    sProduit.createFeatureAttributeValue(nature34, "NR");
    FC_FeatureAttributeValue nr34 = nature34
        .getFeatureAttributeValueByName("NR");
    nr34.setDefinition("Non renseignée, l’information est manquante dans la base.");

    // Attribut TOPONYME
    sProduit.createFeatureAttribute(paiTransport, "TOPONYME", "string", false);
    AttributeType top34 = paiTransport.getFeatureAttributeByName("TOPONYME");
    top34
        .setDefinition("Nom validé par l’Equipe Produit Toponymie qui garantit son orthographe.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(paiTransport, "IMPORTANCE", "string", true);
    AttributeType importance34 = paiTransport
        .getFeatureAttributeByName("IMPORTANCE");
    importance34
        .setDefinition("Importance du toponyme dans son environnement.");
    sProduit.createFeatureAttributeValue(importance34, "1");
    sProduit.createFeatureAttributeValue(importance34, "2");
    sProduit.createFeatureAttributeValue(importance34, "3");
    sProduit.createFeatureAttributeValue(importance34, "4");
    sProduit.createFeatureAttributeValue(importance34, "5");
    sProduit.createFeatureAttributeValue(importance34, "6");
    sProduit.createFeatureAttributeValue(importance34, "7");
    sProduit.createFeatureAttributeValue(importance34, "8");
    sProduit.createFeatureAttributeValue(importance34, "NC");
    sProduit.createFeatureAttributeValue(importance34, "NR");

    // Classe
    // PAI_ZONE_HABITATION///////////////////////////////////////////////////

    sProduit.createFeatureType("PAI_ZONE_HABITATION");
    FeatureType paiZoneHab = (FeatureType) (sProduit
        .getFeatureTypeByName("PAI_ZONE_HABITATION"));
    paiZoneHab
        .setDefinition("Désignation d’un lieu-dit habité caractérisé par un nom.");
    paiZoneHab.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(paiZoneHab, "ID", "string", false);
    AttributeType id35 = paiZoneHab.getFeatureAttributeByName("ID");
    id35.setDefinition("Identifiant du PAI. Cet identifiant est unique. Il est stable d’une édition à l’autre.");

    // Attribut ORIGINE
    sProduit.createFeatureAttribute(paiZoneHab, "ORIGINE", "string", true);
    AttributeType origine35 = paiZoneHab.getFeatureAttributeByName("ORIGINE");
    origine35.setDefinition("Source de l'information.");
    sProduit.createFeatureAttributeValue(origine35, "Plan");
    sProduit.createFeatureAttributeValue(origine35, "Fichier");
    sProduit.createFeatureAttributeValue(origine35, "Terrain");
    sProduit.createFeatureAttributeValue(origine35, "Scan25");
    sProduit.createFeatureAttributeValue(origine35, "Orthophotographie");
    sProduit.createFeatureAttributeValue(origine35, "BDTopo");
    sProduit.createFeatureAttributeValue(origine35, "BDCarto");
    sProduit.createFeatureAttributeValue(origine35, "Géoroute");
    sProduit.createFeatureAttributeValue(origine35, "BDNyme");
    sProduit.createFeatureAttributeValue(origine35, "Calculé");
    sProduit.createFeatureAttributeValue(origine35, "BDParcellaire");
    sProduit.createFeatureAttributeValue(origine35, "NR");

    // Attribut NATURE
    sProduit.createFeatureAttribute(paiZoneHab, "NATURE", "string", true);
    AttributeType nature35 = paiZoneHab.getFeatureAttributeByName("NATURE");
    nature35.setDefinition("Nature du PAI.");
    sProduit.createFeatureAttributeValue(nature35, "Château");
    FC_FeatureAttributeValue chateau35 = nature35
        .getFeatureAttributeValueByName("Château");
    chateau35
        .setDefinition("Château ou tour. Le lieu-dit, toujours nommé, peut ne pas être habité ou ne plus être habité mais n’est pas totalement en ruines.");
    sProduit.createFeatureAttributeValue(nature35, "Grange");
    FC_FeatureAttributeValue grange35 = nature35
        .getFeatureAttributeValueByName("Grange");
    grange35
        .setDefinition("Construction légère : abri, baraquement, cabane, grange, hangar.");
    sProduit.createFeatureAttributeValue(nature35, "Lieu-dit habité");
    FC_FeatureAttributeValue lieuDitHab35 = nature35
        .getFeatureAttributeValueByName("Lieu-dit habité");
    lieuDitHab35
        .setDefinition("Groupe d’habitations nommé situé en dehors du chef-lieu de commune : hameau, habitation isolée, ancien chef-lieu de commune.");
    sProduit.createFeatureAttributeValue(nature35, "Moulin");
    FC_FeatureAttributeValue moulin35 = nature35
        .getFeatureAttributeValueByName("Moulin");
    moulin35.setDefinition("Moulin ou ancien moulin à eau.");
    sProduit.createFeatureAttributeValue(nature35, "Quartier");
    FC_FeatureAttributeValue quartier35 = nature35
        .getFeatureAttributeValueByName("Quartier");
    quartier35.setDefinition("Quartier nommé : cité, faubourg, lotissement.");
    sProduit.createFeatureAttributeValue(nature35, "Ruines");
    FC_FeatureAttributeValue ruines35 = nature35
        .getFeatureAttributeValueByName("Ruines");
    ruines35.setDefinition("Bâtiment ou construction en ruines.");
    sProduit.createFeatureAttributeValue(nature35, "NR");
    FC_FeatureAttributeValue nr35 = nature35
        .getFeatureAttributeValueByName("NR");
    nr35.setDefinition("Non renseignée, l’information est manquante dans la base.");

    // Attribut TOPONYME
    sProduit.createFeatureAttribute(paiZoneHab, "TOPONYME", "string", false);
    AttributeType top35 = paiZoneHab.getFeatureAttributeByName("TOPONYME");
    top35
        .setDefinition("Nom validé par l’Equipe Produit Toponymie qui garantit son orthographe.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(paiZoneHab, "IMPORTANCE", "string", true);
    AttributeType importance35 = paiZoneHab
        .getFeatureAttributeByName("IMPORTANCE");
    importance35
        .setDefinition("Importance du toponyme dans son environnement.");
    sProduit.createFeatureAttributeValue(importance35, "1");
    sProduit.createFeatureAttributeValue(importance35, "2");
    sProduit.createFeatureAttributeValue(importance35, "3");
    sProduit.createFeatureAttributeValue(importance35, "4");
    sProduit.createFeatureAttributeValue(importance35, "5");
    sProduit.createFeatureAttributeValue(importance35, "6");
    sProduit.createFeatureAttributeValue(importance35, "7");
    sProduit.createFeatureAttributeValue(importance35, "8");
    sProduit.createFeatureAttributeValue(importance35, "NC");
    sProduit.createFeatureAttributeValue(importance35, "NR");

    // Classe
    // PAI_HYDROGRAPHIE///////////////////////////////////////////////////

    sProduit.createFeatureType("PAI_HYDROGRAPHIE");
    FeatureType paiHydro = (FeatureType) (sProduit
        .getFeatureTypeByName("PAI_HYDROGRAPHIE"));
    paiHydro
        .setDefinition("Désignation se rapportant à un détail hydrographique.");
    paiHydro.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(paiHydro, "ID", "string", false);
    AttributeType id36 = paiHydro.getFeatureAttributeByName("ID");
    id36.setDefinition("Identifiant du PAI. Cet identifiant est unique. Il est stable d’une édition à l’autre.");

    // Attribut ORIGINE
    sProduit.createFeatureAttribute(paiHydro, "ORIGINE", "string", true);
    AttributeType origine36 = paiHydro.getFeatureAttributeByName("ORIGINE");
    origine36.setDefinition("Source de l'information.");
    sProduit.createFeatureAttributeValue(origine36, "Plan");
    sProduit.createFeatureAttributeValue(origine36, "Fichier");
    sProduit.createFeatureAttributeValue(origine36, "Terrain");
    sProduit.createFeatureAttributeValue(origine36, "Scan25");
    sProduit.createFeatureAttributeValue(origine36, "Orthophotographie");
    sProduit.createFeatureAttributeValue(origine36, "BDTopo");
    sProduit.createFeatureAttributeValue(origine36, "BDCarto");
    sProduit.createFeatureAttributeValue(origine36, "Géoroute");
    sProduit.createFeatureAttributeValue(origine36, "BDNyme");
    sProduit.createFeatureAttributeValue(origine36, "Calculé");
    sProduit.createFeatureAttributeValue(origine36, "BDParcellaire");
    sProduit.createFeatureAttributeValue(origine36, "NR");

    // Attribut NATURE
    sProduit.createFeatureAttribute(paiHydro, "NATURE", "string", true);
    AttributeType nature36 = paiHydro.getFeatureAttributeByName("NATURE");
    nature36.setDefinition("Nature du PAI.");
    sProduit.createFeatureAttributeValue(nature36, "Amer");
    FC_FeatureAttributeValue amer36 = nature36
        .getFeatureAttributeValueByName("Amer");
    amer36
        .setDefinition("Point de repère visible de la mer : amer, bouée, balise, phare, feu, tourelle.");
    sProduit.createFeatureAttributeValue(nature36, "Baie");
    FC_FeatureAttributeValue baie36 = nature36
        .getFeatureAttributeValueByName("Baie");
    baie36
        .setDefinition("Espace marin pénétrant entre les terres : anse, baie, calanque, crique, golfe.");
    sProduit.createFeatureAttributeValue(nature36, "Banc");
    FC_FeatureAttributeValue banc36 = nature36
        .getFeatureAttributeValueByName("Banc");
    banc36
        .setDefinition("En mer ou sur un fleuve, relief sous-marin non rocheux représentant un danger potentiel pour la navigation : banc, hauts-fonds.");
    sProduit.createFeatureAttributeValue(nature36, "Canal");
    FC_FeatureAttributeValue canal36 = nature36
        .getFeatureAttributeValueByName("Canal");
    canal36
        .setDefinition("Cours d’eau artificiel : ancien canal, bief, canal, fossé, rigole.");
    sProduit.createFeatureAttributeValue(nature36, "Cascade");
    FC_FeatureAttributeValue cascade36 = nature36
        .getFeatureAttributeValueByName("Cascade");
    cascade36.setDefinition("Cascade, chute d’eau.");
    sProduit.createFeatureAttributeValue(nature36, "Embouchure");
    FC_FeatureAttributeValue embouchure36 = nature36
        .getFeatureAttributeValueByName("Embouchure");
    embouchure36
        .setDefinition("Embouchure d’un fleuve : delta, embouchure, estuaire.");
    sProduit.createFeatureAttributeValue(nature36, "Espace maritime");
    FC_FeatureAttributeValue espaceM36 = nature36
        .getFeatureAttributeValueByName("Espace maritime");
    espaceM36.setDefinition("Espace maritime, mer, océan, passe.");
    sProduit.createFeatureAttributeValue(nature36, "Glacier");
    FC_FeatureAttributeValue glacier36 = nature36
        .getFeatureAttributeValueByName("Glacier");
    glacier36
        .setDefinition("Nom d’un glacier ou d’un détail relatif à un glacier : crevasse, glacier, moraine, névé, sérac.");
    sProduit.createFeatureAttributeValue(nature36, "Lac");
    FC_FeatureAttributeValue lac36 = nature36
        .getFeatureAttributeValueByName("Lac");
    lac36.setDefinition("Étendue d’eau terrestre : bassin, étang, lac, mare.");
    sProduit.createFeatureAttributeValue(nature36, "Marais");
    FC_FeatureAttributeValue marais36 = nature36
        .getFeatureAttributeValueByName("Marais");
    marais36.setDefinition("Zone humide : marais, marécage, saline.");
    sProduit.createFeatureAttributeValue(nature36, "Pêcherie");
    FC_FeatureAttributeValue pecherie36 = nature36
        .getFeatureAttributeValueByName("Pêcherie");
    pecherie36
        .setDefinition("Zone d’activité aquacole : bouchot, parc à huîtres, pêcherie.");
    sProduit.createFeatureAttributeValue(nature36, "Perte");
    FC_FeatureAttributeValue perte36 = nature36
        .getFeatureAttributeValueByName("Perte");
    perte36
        .setDefinition("Lieu où disparaît, où se perd un cours d’eau, qui réapparaît ensuite, en formant une résurgence, après avoir effectué un trajet souterrain.");
    sProduit.createFeatureAttributeValue(nature36, "Point d'eau");
    FC_FeatureAttributeValue pointEau36 = nature36
        .getFeatureAttributeValueByName("Point d'eau");
    pointEau36
        .setDefinition("Tout point d’eau naturel ou artificiel : captage, citerne, fontaine, puits, résurgence, source.");
    sProduit.createFeatureAttributeValue(nature36, "Rivière");
    FC_FeatureAttributeValue riviere36 = nature36
        .getFeatureAttributeValueByName("Rivière");
    riviere36
        .setDefinition("Zone d’activité aquacole : bouchot, parc à huîtres, pêcherie.");
    sProduit.createFeatureAttributeValue(nature36, "NR");
    FC_FeatureAttributeValue nr36 = nature36
        .getFeatureAttributeValueByName("NR");
    nr36.setDefinition("Non renseignée, l’information est manquante dans la base.");

    // Attribut TOPONYME
    sProduit.createFeatureAttribute(paiHydro, "TOPONYME", "string", false);
    AttributeType top36 = paiHydro.getFeatureAttributeByName("TOPONYME");
    top36
        .setDefinition("Nom validé par l’Equipe Produit Toponymie qui garantit son orthographe.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(paiHydro, "IMPORTANCE", "string", true);
    AttributeType importance36 = paiHydro
        .getFeatureAttributeByName("IMPORTANCE");
    importance36
        .setDefinition("Importance du toponyme dans son environnement.");
    sProduit.createFeatureAttributeValue(importance36, "1");
    sProduit.createFeatureAttributeValue(importance36, "2");
    sProduit.createFeatureAttributeValue(importance36, "3");
    sProduit.createFeatureAttributeValue(importance36, "4");
    sProduit.createFeatureAttributeValue(importance36, "5");
    sProduit.createFeatureAttributeValue(importance36, "6");
    sProduit.createFeatureAttributeValue(importance36, "7");
    sProduit.createFeatureAttributeValue(importance36, "8");
    sProduit.createFeatureAttributeValue(importance36, "NC");
    sProduit.createFeatureAttributeValue(importance36, "NR");

    // Classe
    // PAI_OROGRAPHIE///////////////////////////////////////////////////

    sProduit.createFeatureType("PAI_OROGRAPHIE");
    FeatureType paiOro = (FeatureType) (sProduit
        .getFeatureTypeByName("PAI_OROGRAPHIE"));
    paiOro.setDefinition("Désignation d’un détail du relief.");
    paiOro.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(paiOro, "ID", "string", false);
    AttributeType id37 = paiOro.getFeatureAttributeByName("ID");
    id37.setDefinition("Identifiant du PAI. Cet identifiant est unique. Il est stable d’une édition à l’autre.");

    // Attribut ORIGINE
    sProduit.createFeatureAttribute(paiOro, "ORIGINE", "string", true);
    AttributeType origine37 = paiOro.getFeatureAttributeByName("ORIGINE");
    origine37.setDefinition("Source de l'information.");
    sProduit.createFeatureAttributeValue(origine37, "Plan");
    sProduit.createFeatureAttributeValue(origine37, "Fichier");
    sProduit.createFeatureAttributeValue(origine37, "Terrain");
    sProduit.createFeatureAttributeValue(origine37, "Scan25");
    sProduit.createFeatureAttributeValue(origine37, "Orthophotographie");
    sProduit.createFeatureAttributeValue(origine37, "BDTopo");
    sProduit.createFeatureAttributeValue(origine37, "BDCarto");
    sProduit.createFeatureAttributeValue(origine37, "Géoroute");
    sProduit.createFeatureAttributeValue(origine37, "BDNyme");
    sProduit.createFeatureAttributeValue(origine37, "Calculé");
    sProduit.createFeatureAttributeValue(origine37, "BDParcellaire");
    sProduit.createFeatureAttributeValue(origine37, "NR");

    // Attribut NATURE
    sProduit.createFeatureAttribute(paiOro, "NATURE", "string", true);
    AttributeType nature37 = paiOro.getFeatureAttributeByName("NATURE");
    nature37.setDefinition("Nature du PAI.");
    sProduit.createFeatureAttributeValue(nature37, "Cap");
    FC_FeatureAttributeValue cap37 = nature37
        .getFeatureAttributeValueByName("Cap");
    cap37
        .setDefinition("Prédominance dans le contour d’une côte : cap, pointe, promontoire.");
    sProduit.createFeatureAttributeValue(nature37, "Cirque");
    FC_FeatureAttributeValue cirque37 = nature37
        .getFeatureAttributeValueByName("Cirque");
    cirque37.setDefinition("Dépression semi-circulaire, à bords raides.");
    sProduit.createFeatureAttributeValue(nature37, "Col");
    FC_FeatureAttributeValue col37 = nature37
        .getFeatureAttributeValueByName("Col");
    col37
        .setDefinition("Point de passage imposé par la configuration du relief : col, passage.");
    sProduit.createFeatureAttributeValue(nature37, "Crête");
    FC_FeatureAttributeValue crete37 = nature37
        .getFeatureAttributeValueByName("Crête");
    crete37
        .setDefinition("Ligne de partage des eaux : crête, arête, ligne de faîte.");
    sProduit.createFeatureAttributeValue(nature37, "Dépression");
    FC_FeatureAttributeValue depression37 = nature37
        .getFeatureAttributeValueByName("Dépression");
    depression37
        .setDefinition("Dépression naturelle du sol : cuvette, bassin fermé, dépression, doline.");
    sProduit.createFeatureAttributeValue(nature37, "Dune");
    FC_FeatureAttributeValue dune37 = nature37
        .getFeatureAttributeValueByName("Dune");
    dune37.setDefinition("Monticule de sable sur les bords de la mer.");
    sProduit.createFeatureAttributeValue(nature37, "Escarpement");
    FC_FeatureAttributeValue escarpement37 = nature37
        .getFeatureAttributeValueByName("Escarpement");
    escarpement37
        .setDefinition("Escarpement du relief : barre rocheuse, escarpement rocheux, face abrupte, falaise.");
    sProduit.createFeatureAttributeValue(nature37, "Gorge");
    FC_FeatureAttributeValue gorge37 = nature37
        .getFeatureAttributeValueByName("Gorge");
    gorge37
        .setDefinition("Vallée étroite et encaissée : canyon, cluse, défilé, gorge.");
    sProduit.createFeatureAttributeValue(nature37, "Grotte");
    FC_FeatureAttributeValue grotte37 = nature37
        .getFeatureAttributeValueByName("Grotte");
    grotte37
        .setDefinition("Grotte naturelle ou excavation : aven, cave, gouffre, grotte.");
    sProduit.createFeatureAttributeValue(nature37, "Ile");
    FC_FeatureAttributeValue ile37 = nature37
        .getFeatureAttributeValueByName("Ile");
    ile37.setDefinition("Ile, îlot ou presqu’île.");
    sProduit.createFeatureAttributeValue(nature37, "Isthme");
    FC_FeatureAttributeValue isthme37 = nature37
        .getFeatureAttributeValueByName("Isthme");
    isthme37
        .setDefinition("Bande de terre étroite entre deux mers, réunissant deux terres : cordon littoral, isthme.");
    sProduit.createFeatureAttributeValue(nature37, "Montagne");
    FC_FeatureAttributeValue montagne37 = nature37
        .getFeatureAttributeValueByName("Montagne");
    montagne37
        .setDefinition("Désigne une montagne ou un massif de manière globale et non un sommet en particulier (voir sommet).");
    sProduit.createFeatureAttributeValue(nature37, "Pic");
    FC_FeatureAttributeValue pic37 = nature37
        .getFeatureAttributeValueByName("Pic");
    pic37.setDefinition("Sommet pointu d’une montagne : aiguille, pic, piton.");
    sProduit.createFeatureAttributeValue(nature37, "Plage");
    FC_FeatureAttributeValue plage37 = nature37
        .getFeatureAttributeValueByName("Plage");
    plage37
        .setDefinition("Zone littorale marquée par le flux et le reflux des marées : grève, plage.");
    sProduit.createFeatureAttributeValue(nature37, "Plaine");
    FC_FeatureAttributeValue plaine37 = nature37
        .getFeatureAttributeValueByName("Plaine");
    plaine37
        .setDefinition("Zone de surface terrestre caractérisée par une relative planéité : plaine, plateau.");
    sProduit.createFeatureAttributeValue(nature37, "Récif");
    FC_FeatureAttributeValue recif37 = nature37
        .getFeatureAttributeValueByName("Récif");
    recif37
        .setDefinition("Rocher situé en mer ou dans un fleuve, mais dont une partie, faiblement émergée, peut constituer un obstacle ou un repère : brisant, récif, rocher marin.");
    sProduit.createFeatureAttributeValue(nature37, "Rochers");
    FC_FeatureAttributeValue rochers37 = nature37
        .getFeatureAttributeValueByName("Rochers");
    rochers37
        .setDefinition("Zone ou détail caractérisé par une nature rocheuse mais non verticale : chaos, éboulis, pierrier, rocher.");
    sProduit.createFeatureAttributeValue(nature37, "Sommet");
    FC_FeatureAttributeValue sommet37 = nature37
        .getFeatureAttributeValueByName("Sommet");
    sommet37
        .setDefinition("Point haut du relief non caractérisé par un profil abrupt (voir la nature Pic) : colline, mamelon, mont, sommet.");
    sProduit.createFeatureAttributeValue(nature37, "Vallée");
    FC_FeatureAttributeValue vallee37 = nature37
        .getFeatureAttributeValueByName("Vallée");
    vallee37
        .setDefinition("Espace entre deux ou plusieurs montagnes. Forme définie par la convergence des versants et qui est, ou a été parcourue par un cours d'eau : combe, ravin, val, vallée, vallon, thalweg.");
    sProduit.createFeatureAttributeValue(nature37, "Versant");
    FC_FeatureAttributeValue versant37 = nature37
        .getFeatureAttributeValueByName("Versant");
    versant37
        .setDefinition("Plan incliné joignant une ligne de crête à un thalweg : coteau, versant.");
    sProduit.createFeatureAttributeValue(nature37, "Volcan");
    FC_FeatureAttributeValue volcan37 = nature37
        .getFeatureAttributeValueByName("Volcan");
    volcan37
        .setDefinition("Toute forme de relief témoignant d’une activité volcanique : cratère, volcan.");
    sProduit.createFeatureAttributeValue(nature37, "NR");
    FC_FeatureAttributeValue nr37 = nature37
        .getFeatureAttributeValueByName("NR");
    nr37.setDefinition("Non renseignée, l’information est manquante dans la base.");

    // Attribut TOPONYME
    sProduit.createFeatureAttribute(paiOro, "TOPONYME", "string", false);
    AttributeType top37 = paiOro.getFeatureAttributeByName("TOPONYME");
    top37
        .setDefinition("Nom validé par l’Equipe Produit Toponymie qui garantit son orthographe.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(paiOro, "IMPORTANCE", "string", true);
    AttributeType importance37 = paiOro.getFeatureAttributeByName("IMPORTANCE");
    importance37
        .setDefinition("Importance du toponyme dans son environnement.");
    sProduit.createFeatureAttributeValue(importance37, "1");
    sProduit.createFeatureAttributeValue(importance37, "2");
    sProduit.createFeatureAttributeValue(importance37, "3");
    sProduit.createFeatureAttributeValue(importance37, "4");
    sProduit.createFeatureAttributeValue(importance37, "5");
    sProduit.createFeatureAttributeValue(importance37, "6");
    sProduit.createFeatureAttributeValue(importance37, "7");
    sProduit.createFeatureAttributeValue(importance37, "8");
    sProduit.createFeatureAttributeValue(importance37, "NC");
    sProduit.createFeatureAttributeValue(importance37, "NR");

    // Classe
    // LIEU_DIT_HABITE///////////////////////////////////////////////////

    sProduit.createFeatureType("LIEU_DIT_HABITE");
    FeatureType lieuDitHab = (FeatureType) (sProduit
        .getFeatureTypeByName("LIEU_DIT_HABITE"));
    lieuDitHab.setDefinition("Lieu-dit habité caractérisé par un nom.");
    lieuDitHab.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(lieuDitHab, "ID", "string", false);
    AttributeType id38 = lieuDitHab.getFeatureAttributeByName("ID");
    id38.setDefinition("Identifiant du lieu-dit. Cet identifiant est unique. Il est stable d’une édition à l’autre.");

    // Attribut ORIGINE_NOM
    sProduit.createFeatureAttribute(lieuDitHab, "ORIGINE_NOM", "string", true);
    AttributeType origine38 = lieuDitHab
        .getFeatureAttributeByName("ORIGINE_NOM");
    origine38
        .setDefinition("Origine du toponyme. Attribut précisant l’origine de la donnée.");
    sProduit.createFeatureAttributeValue(origine38, "Scan25");
    FC_FeatureAttributeValue scan38 = origine38
        .getFeatureAttributeValueByName("Scan25");
    scan38.setDefinition("Carte IGN au 1 : 25 000.");
    sProduit.createFeatureAttributeValue(origine38, "BDCarto");
    FC_FeatureAttributeValue bdcarto38 = origine38
        .getFeatureAttributeValueByName("BDCarto");
    bdcarto38
        .setDefinition("Base de données BD CARTO® pour la géométrie de l’objet.");
    sProduit.createFeatureAttributeValue(origine38, "BDTopo");
    FC_FeatureAttributeValue bdtopo38 = origine38
        .getFeatureAttributeValueByName("BDTopo");
    bdtopo38
        .setDefinition("Base de données BD TOPO® antérieure à la BD TOPO® Pays.");
    sProduit.createFeatureAttributeValue(origine38, "BDNyme");
    FC_FeatureAttributeValue bdnyme38 = origine38
        .getFeatureAttributeValueByName("BDNyme");
    bdnyme38.setDefinition("Base de données BD NYME®.");
    sProduit.createFeatureAttributeValue(origine38, "Géoroute");
    FC_FeatureAttributeValue georoute38 = origine38
        .getFeatureAttributeValueByName("Géoroute");
    georoute38
        .setDefinition("Base de données GEOROUTE® pour la géométrie de l’objet (notamment les points et surfaces d’activité sur les zones couvertes par GEOROUTE®).");
    sProduit.createFeatureAttributeValue(origine38, "Fichier");
    FC_FeatureAttributeValue fichier38 = origine38
        .getFeatureAttributeValueByName("Fichier");
    fichier38
        .setDefinition("Fichier numérique obtenu auprès d’un prestataire extérieur à l’IGN.");
    sProduit.createFeatureAttributeValue(origine38, "Plan");
    FC_FeatureAttributeValue plan38 = origine38
        .getFeatureAttributeValueByName("Plan");
    plan38
        .setDefinition("Plan qui a été reporté ou documentation aidant à la localisation.");
    sProduit.createFeatureAttributeValue(origine38, "BDParcellaire");
    FC_FeatureAttributeValue bdparcellaire38 = origine38
        .getFeatureAttributeValueByName("BDParcellaire");
    bdparcellaire38.setDefinition("Base de données BD PARCELLAIRE®.");
    sProduit.createFeatureAttributeValue(origine38, "Terrain");
    FC_FeatureAttributeValue terrain38 = origine38
        .getFeatureAttributeValueByName("Terrain");
    terrain38
        .setDefinition("Information provenant d’un passage sur le terrain.");
    sProduit.createFeatureAttributeValue(origine38, "NR");
    FC_FeatureAttributeValue nr38 = origine38
        .getFeatureAttributeValueByName("NR");
    nr38.setDefinition("Non renseigné");

    // Attribut NOM
    sProduit.createFeatureAttribute(lieuDitHab, "NOM", "string", false);
    AttributeType nom38 = lieuDitHab.getFeatureAttributeByName("NOM");
    nom38
        .setDefinition("Orthographe du toponyme validée par le bureau de Toponymie.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(lieuDitHab, "IMPORTANCE", "string", true);
    AttributeType importance38 = lieuDitHab
        .getFeatureAttributeByName("IMPORTANCE");
    importance38
        .setDefinition("Importance du toponyme dans son environnement.");
    sProduit.createFeatureAttributeValue(importance38, "1");
    FC_FeatureAttributeValue un38 = importance38
        .getFeatureAttributeValueByName("1");
    un38.setDefinition("lieu habité de plus de 100 000 habitants");
    sProduit.createFeatureAttributeValue(importance38, "2");
    FC_FeatureAttributeValue deux38 = importance38
        .getFeatureAttributeValueByName("2");
    deux38.setDefinition("lieu habité de 25 000 à 100 000 habitants");
    sProduit.createFeatureAttributeValue(importance38, "3");
    FC_FeatureAttributeValue trois38 = importance38
        .getFeatureAttributeValueByName("3");
    trois38.setDefinition("lieu habité de 5 000 à 25 000 habitants");
    sProduit.createFeatureAttributeValue(importance38, "4");
    FC_FeatureAttributeValue quatre38 = importance38
        .getFeatureAttributeValueByName("4");
    quatre38.setDefinition("lieu habité de 1 000 à 5 000 habitants");
    sProduit.createFeatureAttributeValue(importance38, "5");
    FC_FeatureAttributeValue cinq38 = importance38
        .getFeatureAttributeValueByName("5");
    cinq38.setDefinition("lieu habité de 200 à 1 000 habitants");
    sProduit.createFeatureAttributeValue(importance38, "6");
    FC_FeatureAttributeValue six38 = importance38
        .getFeatureAttributeValueByName("6");
    six38
        .setDefinition("lieu habité de moins de 200 habitants, quartier de ville");
    sProduit.createFeatureAttributeValue(importance38, "7");
    FC_FeatureAttributeValue sept38 = importance38
        .getFeatureAttributeValueByName("7");
    sept38
        .setDefinition("groupe d'habitations (2 à 10 feux, 4 à 20 bâtiments, petit quartier de ville)");
    sProduit.createFeatureAttributeValue(importance38, "8");
    FC_FeatureAttributeValue huit38 = importance38
        .getFeatureAttributeValueByName("8");
    huit38.setDefinition("constructions isolées (1 feu, 1 à 3 bâtiments)");
    sProduit.createFeatureAttributeValue(importance38, "NC");
    FC_FeatureAttributeValue nc38 = importance38
        .getFeatureAttributeValueByName("NC");
    nc38.setDefinition("Non concerné");
    sProduit.createFeatureAttributeValue(importance38, "NR");
    FC_FeatureAttributeValue nrbis38 = importance38
        .getFeatureAttributeValueByName("NR");
    nrbis38.setDefinition("Non renseigné");

    // Attribut NATURE
    sProduit.createFeatureAttribute(lieuDitHab, "NATURE", "string", true);
    AttributeType nature38 = lieuDitHab.getFeatureAttributeByName("NATURE");
    nature38
        .setDefinition("Indique la catégorie à laquelle appartient le lieu-dit.");
    sProduit.createFeatureAttributeValue(nature38, "Château");
    sProduit.createFeatureAttributeValue(nature38, "Grange");
    sProduit.createFeatureAttributeValue(nature38, "Lieu-dit habité");
    sProduit.createFeatureAttributeValue(nature38, "Moulin");
    sProduit.createFeatureAttributeValue(nature38, "Quartier");
    sProduit.createFeatureAttributeValue(nature38, "Refuge");
    sProduit.createFeatureAttributeValue(nature38, "Ruines");

    // Classe
    // LIEU_DIT_NON_HABITE///////////////////////////////////////////////////

    sProduit.createFeatureType("LIEU_DIT_NON_HABITE");
    FeatureType lieuDitNonHab = (FeatureType) (sProduit
        .getFeatureTypeByName("LIEU_DIT_NON_HABITE"));
    lieuDitNonHab.setDefinition("Lieu-dit non habité caractérisé par un nom.");
    lieuDitNonHab.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(lieuDitNonHab, "ID", "string", false);
    AttributeType id39 = lieuDitNonHab.getFeatureAttributeByName("ID");
    id39.setDefinition("Identifiant du lieu-dit. Cet identifiant est unique. Il est stable d’une édition à l’autre.");

    // Attribut ORIGINE_NOM
    sProduit.createFeatureAttribute(lieuDitNonHab, "ORIGINE_NOM", "string",
        true);
    AttributeType origine39 = lieuDitNonHab
        .getFeatureAttributeByName("ORIGINE_NOM");
    origine39
        .setDefinition("Origine du toponyme. Attribut précisant l’origine de la donnée.");
    sProduit.createFeatureAttributeValue(origine39, "Scan25");
    FC_FeatureAttributeValue scan39 = origine39
        .getFeatureAttributeValueByName("Scan25");
    scan39.setDefinition("Carte IGN au 1 : 25 000.");
    sProduit.createFeatureAttributeValue(origine39, "BDCarto");
    FC_FeatureAttributeValue bdcarto39 = origine39
        .getFeatureAttributeValueByName("BDCarto");
    bdcarto39
        .setDefinition("Base de données BD CARTO® pour la géométrie de l’objet.");
    sProduit.createFeatureAttributeValue(origine39, "BDTopo");
    FC_FeatureAttributeValue bdtopo39 = origine39
        .getFeatureAttributeValueByName("BDTopo");
    bdtopo39
        .setDefinition("Base de données BD TOPO® antérieure à la BD TOPO® Pays.");
    sProduit.createFeatureAttributeValue(origine39, "BDNyme");
    FC_FeatureAttributeValue bdnyme39 = origine39
        .getFeatureAttributeValueByName("BDNyme");
    bdnyme39.setDefinition("Base de données BD NYME®.");
    sProduit.createFeatureAttributeValue(origine39, "Géoroute");
    FC_FeatureAttributeValue georoute39 = origine39
        .getFeatureAttributeValueByName("Géoroute");
    georoute39
        .setDefinition("Base de données GEOROUTE® pour la géométrie de l’objet (notamment les points et surfaces d’activité sur les zones couvertes par GEOROUTE®).");
    sProduit.createFeatureAttributeValue(origine39, "Fichier");
    FC_FeatureAttributeValue fichier39 = origine39
        .getFeatureAttributeValueByName("Fichier");
    fichier39
        .setDefinition("Fichier numérique obtenu auprès d’un prestataire extérieur à l’IGN.");
    sProduit.createFeatureAttributeValue(origine39, "Plan");
    FC_FeatureAttributeValue plan39 = origine39
        .getFeatureAttributeValueByName("Plan");
    plan39
        .setDefinition("Plan qui a été reporté ou documentation aidant à la localisation.");
    sProduit.createFeatureAttributeValue(origine39, "BDParcellaire");
    FC_FeatureAttributeValue bdparcellaire39 = origine39
        .getFeatureAttributeValueByName("BDParcellaire");
    bdparcellaire39.setDefinition("Base de données BD PARCELLAIRE®.");
    sProduit.createFeatureAttributeValue(origine39, "Terrain");
    FC_FeatureAttributeValue terrain39 = origine39
        .getFeatureAttributeValueByName("Terrain");
    terrain39
        .setDefinition("Information provenant d’un passage sur le terrain.");
    sProduit.createFeatureAttributeValue(origine39, "NR");
    FC_FeatureAttributeValue nr39 = origine39
        .getFeatureAttributeValueByName("NR");
    nr39.setDefinition("Non renseigné");

    // Attribut NOM
    sProduit.createFeatureAttribute(lieuDitNonHab, "NOM", "string", false);
    AttributeType nom39 = lieuDitNonHab.getFeatureAttributeByName("NOM");
    nom39
        .setDefinition("Orthographe du toponyme validée par le bureau de Toponymie.");

    // Attribut IMPORTANCE
    sProduit
        .createFeatureAttribute(lieuDitNonHab, "IMPORTANCE", "string", true);
    AttributeType importance39 = lieuDitNonHab
        .getFeatureAttributeByName("IMPORTANCE");
    importance39
        .setDefinition("Importance du toponyme dans son environnement.");
    sProduit.createFeatureAttributeValue(importance39, "1");
    FC_FeatureAttributeValue un39 = importance39
        .getFeatureAttributeValueByName("1");
    un39.setDefinition("Lieu-dit non habité de plus 20 Km²");
    sProduit.createFeatureAttributeValue(importance39, "2");
    FC_FeatureAttributeValue deux39 = importance39
        .getFeatureAttributeValueByName("2");
    deux39.setDefinition("Lieu-dit habité de 10 à 20 Km²");
    sProduit.createFeatureAttributeValue(importance39, "3");
    FC_FeatureAttributeValue trois39 = importance39
        .getFeatureAttributeValueByName("3");
    trois39.setDefinition("Lieu-dit non habité de 5 à 10 Km²");
    sProduit.createFeatureAttributeValue(importance39, "4");
    FC_FeatureAttributeValue quatre39 = importance39
        .getFeatureAttributeValueByName("4");
    quatre39.setDefinition("Lieu-dit non habité de 2 à 5 Km²");
    sProduit.createFeatureAttributeValue(importance39, "5");
    FC_FeatureAttributeValue cinq39 = importance39
        .getFeatureAttributeValueByName("5");
    cinq39.setDefinition("Lieu-dit non habité de 1 à 2 Km²");
    sProduit.createFeatureAttributeValue(importance39, "6");
    FC_FeatureAttributeValue six39 = importance39
        .getFeatureAttributeValueByName("6");
    six39.setDefinition("Lieu-dit non habité de 0,25 à 1 Km²");
    sProduit.createFeatureAttributeValue(importance39, "7");
    FC_FeatureAttributeValue sept39 = importance39
        .getFeatureAttributeValueByName("7");
    sept39.setDefinition("Lieu-dit non habité de moins de 0 ,25 Km²");
    sProduit.createFeatureAttributeValue(importance39, "8");
    FC_FeatureAttributeValue huit39 = importance39
        .getFeatureAttributeValueByName("8");
    huit39
        .setDefinition("Lieu-dit non habité de moins de 0 ,25 Km² et jugés moins importants que ceux d’importance 7");
    sProduit.createFeatureAttributeValue(importance39, "NC");
    FC_FeatureAttributeValue nc39 = importance39
        .getFeatureAttributeValueByName("NC");
    nc39.setDefinition("Non concerné");
    sProduit.createFeatureAttributeValue(importance39, "NR");
    FC_FeatureAttributeValue nrbis39 = importance39
        .getFeatureAttributeValueByName("NR");
    nrbis39.setDefinition("Non renseigné");

    // Attribut NATURE
    sProduit.createFeatureAttribute(lieuDitNonHab, "NATURE", "string", true);
    AttributeType nature39 = lieuDitNonHab.getFeatureAttributeByName("NATURE");
    nature39
        .setDefinition("Indique la catégorie à laquelle appartient le lieu-dit.");
    sProduit.createFeatureAttributeValue(nature39, "Barrage");
    sProduit.createFeatureAttributeValue(nature39, "Croix");
    sProduit.createFeatureAttributeValue(nature39, "Tombeau");
    sProduit.createFeatureAttributeValue(nature39, "Digue");
    sProduit.createFeatureAttributeValue(nature39, "Dolmen");
    sProduit.createFeatureAttributeValue(nature39, "Espace public");
    sProduit.createFeatureAttributeValue(nature39, "Habitation troglodytique");
    sProduit.createFeatureAttributeValue(nature39, "Vestiges archéologiques");
    sProduit.createFeatureAttributeValue(nature39, "Lieu-dit non habité");
    sProduit.createFeatureAttributeValue(nature39, "Point de vue");
    sProduit.createFeatureAttributeValue(nature39, "Marais salants");
    sProduit.createFeatureAttributeValue(nature39, "Mine");
    sProduit.createFeatureAttributeValue(nature39, "Ouvrage militaire");

    // Classe
    // TOPONYME_DIVERS///////////////////////////////////////////////////

    sProduit.createFeatureType("TOPONYME_DIVERS");
    FeatureType toponymeDivers = (FeatureType) (sProduit
        .getFeatureTypeByName("TOPONYME_DIVERS"));
    toponymeDivers
        .setDefinition("Toponyme de nature diverse, désignant un bâtiment administratif, ou bien une école, un détail religieux, un établissement de santé... etc.");
    toponymeDivers.setIsAbstract(false);

    // Attribut ID
    sProduit.createFeatureAttribute(toponymeDivers, "ID", "string", false);
    AttributeType id40 = toponymeDivers.getFeatureAttributeByName("ID");
    id40.setDefinition("Identifiant du toponyme. Cet identifiant est unique. Il est stable d’une édition à l’autre.");

    // Attribut ORIGINE_NOM
    sProduit.createFeatureAttribute(toponymeDivers, "ORIGINE_NOM", "string",
        true);
    AttributeType origine40 = toponymeDivers
        .getFeatureAttributeByName("ORIGINE_NOM");
    origine40
        .setDefinition("Origine du toponyme. Attribut précisant l’origine de la donnée.");
    sProduit.createFeatureAttributeValue(origine40, "Scan25");
    FC_FeatureAttributeValue scan40 = origine40
        .getFeatureAttributeValueByName("Scan25");
    scan40.setDefinition("Carte IGN au 1 : 25 000.");
    sProduit.createFeatureAttributeValue(origine40, "BDCarto");
    FC_FeatureAttributeValue bdcarto40 = origine40
        .getFeatureAttributeValueByName("BDCarto");
    bdcarto40
        .setDefinition("Base de données BD CARTO® pour la géométrie de l’objet.");
    sProduit.createFeatureAttributeValue(origine40, "BDTopo");
    FC_FeatureAttributeValue bdtopo40 = origine40
        .getFeatureAttributeValueByName("BDTopo");
    bdtopo40
        .setDefinition("Base de données BD TOPO® antérieure à la BD TOPO® Pays.");
    sProduit.createFeatureAttributeValue(origine40, "BDNyme");
    FC_FeatureAttributeValue bdnyme40 = origine40
        .getFeatureAttributeValueByName("BDNyme");
    bdnyme40.setDefinition("Base de données BD NYME®.");
    sProduit.createFeatureAttributeValue(origine40, "Géoroute");
    FC_FeatureAttributeValue georoute40 = origine40
        .getFeatureAttributeValueByName("Géoroute");
    georoute40
        .setDefinition("Base de données GEOROUTE® pour la géométrie de l’objet (notamment les points et surfaces d’activité sur les zones couvertes par GEOROUTE®).");
    sProduit.createFeatureAttributeValue(origine40, "Fichier");
    FC_FeatureAttributeValue fichier40 = origine40
        .getFeatureAttributeValueByName("Fichier");
    fichier40
        .setDefinition("Fichier numérique obtenu auprès d’un prestataire extérieur à l’IGN.");
    sProduit.createFeatureAttributeValue(origine40, "Plan");
    FC_FeatureAttributeValue plan40 = origine40
        .getFeatureAttributeValueByName("Plan");
    plan40
        .setDefinition("Plan qui a été reporté ou documentation aidant à la localisation.");
    sProduit.createFeatureAttributeValue(origine40, "BDParcellaire");
    FC_FeatureAttributeValue bdparcellaire40 = origine40
        .getFeatureAttributeValueByName("BDParcellaire");
    bdparcellaire40.setDefinition("Base de données BD PARCELLAIRE®.");
    sProduit.createFeatureAttributeValue(origine40, "Terrain");
    FC_FeatureAttributeValue terrain40 = origine40
        .getFeatureAttributeValueByName("Terrain");
    terrain40
        .setDefinition("Information provenant d’un passage sur le terrain.");
    sProduit.createFeatureAttributeValue(origine40, "NR");
    FC_FeatureAttributeValue nr40 = origine40
        .getFeatureAttributeValueByName("NR");
    nr40.setDefinition("Non renseigné");

    // Attribut NOM
    sProduit.createFeatureAttribute(toponymeDivers, "NOM", "string", false);
    AttributeType nom40 = toponymeDivers.getFeatureAttributeByName("NOM");
    nom40
        .setDefinition("Orthographe du toponyme validée par le bureau de Toponymie.");

    // Attribut IMPORTANCE
    sProduit.createFeatureAttribute(toponymeDivers, "IMPORTANCE", "string",
        true);
    AttributeType importance40 = toponymeDivers
        .getFeatureAttributeByName("IMPORTANCE");
    importance40
        .setDefinition("Importance du toponyme dans son environnement.");
    sProduit.createFeatureAttributeValue(importance40, "1");
    sProduit.createFeatureAttributeValue(importance40, "2");
    sProduit.createFeatureAttributeValue(importance40, "3");
    sProduit.createFeatureAttributeValue(importance40, "4");
    sProduit.createFeatureAttributeValue(importance40, "5");
    sProduit.createFeatureAttributeValue(importance40, "6");
    sProduit.createFeatureAttributeValue(importance40, "7");
    sProduit.createFeatureAttributeValue(importance40, "8");
    sProduit.createFeatureAttributeValue(importance40, "NC");
    sProduit.createFeatureAttributeValue(importance40, "NR");

    // Attribut NATURE
    sProduit.createFeatureAttribute(toponymeDivers, "NATURE", "string", true);
    AttributeType nature40 = toponymeDivers.getFeatureAttributeByName("NATURE");
    nature40
        .setDefinition("Attribut donnant plus précisément la nature de l'objet nommé.");
    sProduit.createFeatureAttributeValue(nature40, "Enceinte militaire");
    sProduit.createFeatureAttributeValue(nature40,
        "Etablissement pénitenciaire");
    sProduit.createFeatureAttributeValue(nature40, "Maison forestière");
    sProduit.createFeatureAttributeValue(nature40, "Camping");
    sProduit.createFeatureAttributeValue(nature40, "Construction");
    sProduit.createFeatureAttributeValue(nature40, "Maison du parc");
    sProduit.createFeatureAttributeValue(nature40, "Menhir");
    sProduit.createFeatureAttributeValue(nature40, "Monument");
    sProduit.createFeatureAttributeValue(nature40, "Musée");
    sProduit.createFeatureAttributeValue(nature40, "Parc de loisirs");
    sProduit.createFeatureAttributeValue(nature40, "Parc des expositions");
    sProduit.createFeatureAttributeValue(nature40, "Parc zoologique");
    sProduit.createFeatureAttributeValue(nature40, "Village de vacances");
    sProduit.createFeatureAttributeValue(nature40, "Arbre");
    sProduit.createFeatureAttributeValue(nature40, "Bois");
    sProduit.createFeatureAttributeValue(nature40, "Parc");
    sProduit.createFeatureAttributeValue(nature40, "Enseignement supérieur");
    sProduit.createFeatureAttributeValue(nature40, "Science");
    sProduit.createFeatureAttributeValue(nature40, "Centrale électrique");
    sProduit.createFeatureAttributeValue(nature40, "Haras national");
    sProduit.createFeatureAttributeValue(nature40, "Zone industrielle");
    sProduit.createFeatureAttributeValue(nature40, "Hôpital");
    sProduit.createFeatureAttributeValue(nature40, "Etablissement hospitalier");
    sProduit.createFeatureAttributeValue(nature40, "Etablissement thermal");
    sProduit.createFeatureAttributeValue(nature40, "Golf");
    sProduit.createFeatureAttributeValue(nature40, "Hippodrome");
    sProduit.createFeatureAttributeValue(nature40, "Stade");
    sProduit.createFeatureAttributeValue(nature40, "Aéroport militaire");
    sProduit.createFeatureAttributeValue(nature40, "Aéroport non militaire");
    sProduit.createFeatureAttributeValue(nature40, "Aéroport international");
    sProduit.createFeatureAttributeValue(nature40, "Aéroport quelconque");

    // Affichage du schéma
    Integer compteurFT = 0;
    Integer compteurAT = 0;
    Integer compteurV = 0;
    System.out.println("Récapitulons:");
    List<FeatureType> listeFT = sProduit.getFeatureTypes();
    for (FeatureType type : listeFT) {
      compteurFT = compteurFT + 1;
      System.out.println("Classe: " + type.getTypeName());
      List<GF_AttributeType> listeAT = type.getFeatureAttributes();
      for (GF_AttributeType type2 : listeAT) {
        compteurAT = compteurAT + 1;
        System.out.println("Attribut: " + type2.getMemberName());
        if (type2.getValueDomainType()) {
          List<FC_FeatureAttributeValue> listeValeurs = type2.getValuesDomain();
          for (FC_FeatureAttributeValue value : listeValeurs) {
            compteurV = compteurV + 1;
            System.out.println("Valeur: " + value.getLabel());
          }
        }
      }
    }
    System.out.println("Nb de FT = " + compteurFT);
    System.out.println("Nb de AT = " + compteurAT);
    System.out.println("Nb de V = " + compteurV);

    return sProduit;
  }
}
