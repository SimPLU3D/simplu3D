drop table if exists zone_urba ;
drop table if exists road ;
drop table if exists axe ;
drop table if exists cadastral_parcel ;
drop table if exists building_part ;
drop table if exists sub_parcel ;
drop table if exists basic_property_unit ;
drop table if exists doc_urba ;
drop table if exists specific_cboundary ;
drop table if exists building ;
drop table if exists roof ;
drop table if exists roofing ;
drop table if exists gutter ;
drop table if exists gable ;
drop table if exists wall_surface ;
drop table if exists version ;
drop table if exists utilisateur ;
drop table if exists user_version ;


-- Creation de la table Zone Urba :
create table zone_urba(
    zu_id int primary key,
    zu_libelle text,
    zu_libelong text,
    zu_typezone text,
    zu_destdomi text,
    zu_nomfic text,
    zu_urlfic text,
    zu_insee text,
    zu_date_appro date,
    zu_date_valid date,
    zu_commentaire text,
    zu_id_plu text
);
    -- Ajout de la geometrie de la table Zone Urba :
    ALTER TABLE "zone_urba" ADD COLUMN "the_geom" geometry(MultiPolygonZ,2154);

	
-- Creation de la table Road :
create table road(
    road_id int primary key,
    road_nom text,
    road_type text,
    road_largeur numeric
);
    -- Ajout de la geometrie de la table Road :
    ALTER TABLE "road" ADD COLUMN "road_surf" geometry(MultiPolygonZ,2154);


-- Creation de la table Axe :
create table axe(
    axe_id int primary key,
    axe_id_road int
);
    -- Ajout de la geometrie de la table Axe :
    ALTER TABLE "axe" ADD COLUMN "axe_geom" geometry(MultiLinestringZ,2154);


-- Creation de la table Cadastral Parcel :
create table cadastral_parcel(
    cadpar_id int primary key,
    cadpar_num int,
    cadpar_surf numeric,
    cadpar_id_bpu int
);
    -- Ajout de la geometrie de la table Cadastral Parcel :
    ALTER TABLE "cadastral_parcel" ADD COLUMN "the_geom" geometry(MultiPolygonZ,2154);


-- Creation de la table Building Part :
create table building_part(
    buildp_id int primary key,
    buildp_id_build int,
    buildp_id_subpar int,
    buildp_id_version int
);
    -- Ajout de la geometrie  de la table Building Part :
    ALTER TABLE "building_part" ADD COLUMN "buildp_footprint" geometry(Polygon,2154);


-- Creation de la table Sub-Parcelle :
create table sub_parcel(
    subpar_id int primary key,
    subpar_id_cadpar int,
    subpar_id_zu int,
    subpar_avg_slope numeric,
    subpar_surf numeric
);
    -- Ajout de la geometrie de la table Sub_Parcelle :
    ALTER TABLE "sub_parcel" ADD COLUMN "the_geom" geometry(MultiPolygonZ,2154);


-- Creation de la table Basic Property Unit :
create table basic_property_unit(
    bpu_id int primary key
);
    -- Ajout de la geometrie de la table Basic Property Unit :
    ALTER TABLE "basic_property_unit" ADD COLUMN "the_geom" geometry(MultiPolygon,2154);


-- Creation de la table PLU :
create table doc_urba(
    docu_id int primary key,
    docu_id_urba text,
    docu_type_doc text,
    docu_date_appro date,
    docu_date_fin date,
    docu_interco text,
    docu_siren text,
    docu_etat text,
    docu_nom_reg text,
    docu_url_reg text,
    docu_nom_plan text,
    docu_url_plan text,
    docu_site text,
    docu_type_ref text,
    docu_date_ref text
    
);


-- Creation de la table Specific CBoundary :
create table specific_cboundary(
    scb_id int primary key,
    scb_type int,
    scb_side int,
    scb_id_subpar int,
    scb_id_adj int,
    scb_table_ref text
);
    -- Ajout de la geometrie de la table Specific CBoundary :
    ALTER TABLE "specific_cboundary" ADD COLUMN "the_geom" geometry(LinestringZ,2154);


-- Creation de la table Building :
create table building(
    build_id int primary key
);
    -- Ajout de la geometrie de la table Building :
    -- ALTER TABLE "building" ADD COLUMN "the_geom" geometry(MultiPolygonZ,2154);


-- Creation de la table Roof :
create table roof(
    roof_id int primary key,
    roof_angle_min decimal,
    roof_angle_max decimal,
    roof_id_buildp int
);
    -- Ajout de la geometrie de la table Roof :
    ALTER TABLE "roof" ADD COLUMN "the_geom" geometry(MultiPolygonZ,2154);


-- Creation de la table Roofing :
create table roofing(
    roofi_id int primary key,
    roofi_id_roof int
);
    -- Ajout de la geometrie de la table Roofing :
    ALTER TABLE "roofing" ADD COLUMN "the_geom" geometry(MultiLinestringZ,2154);


-- Creation de la table Gutter :
create table gutter(
    gut_id int primary key,
    gut_id_roof int
);
    -- Ajout de la geometrie de la table Gutter :
    ALTER TABLE "gutter" ADD COLUMN "the_geom" geometry(MultiLinestringZ,2154);


-- Creation de la table Gable :
create table gable(
    gab_id int primary key,
    gab_id_roof int
);
    -- Ajout de la geometrie de la table Gable :
    ALTER TABLE "gable" ADD COLUMN "the_geom" geometry(MultiLinestringZ,2154);


-- Creation de la table Wall_Surface :
create table wall_surface(
    wall_id int primary key,
    wall_id_buildp int
);
    -- Ajout de la geometrie de la table Wall Surface :
    ALTER TABLE "wall_surface" ADD COLUMN "the_geom" geometry(MultiPolygonZ,2154);
    
-- Creation de la table Version :
create table version(
    vers_id int primary key,
    vers_id_build_del int,
    vers_id_vers_build int
);

-- Creation de la table User :
create table utilisateur(
    user_id int primary key,
    user_login text,
    user_pw text
);

-- Creation de la table User Version :
create table user_version(
    usv_id int primary key,
    usv_id_user int,
    usv_id_version int,
    usv_nom_version text
);


-- Création de la table rules
drop table if exists rules ;

create table rules(
    rul_id serial,
    rul_nom_zone text,
    rul_bande_incons numeric,
    rul_emp_sol numeric,
    rul_emp_surf_mini numeric,
    rul_emp_larg_mini numeric,
    rul_emp_sol_alt numeric,
    rul_bande_1 numeric,
    rul_alignement numeric,
    rul_recul_lat_mini numeric,
    rul_recul_lat numeric,
    rul_prospect_voirie1_slope numeric,
    rul_prospect_voirie1_hini numeric,
    rul_larg_max_prospect1 numeric,
    rul_prospect_voirie2_slope numeric,
    rul_prospect_voirie2_hini numeric,
    rul_hauteur_maxi_facade numeric,
    rul_bande_2 numeric,
    rul_slope_prospect_lat_slope numeric,
    rul_slope_prospect_lat_hini numeric,
    rul_hauteur_max numeric
);

