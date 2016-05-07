DROP INDEX buildings_idx FORCE; 
DROP INDEX hydrants_idx FORCE; 
DELETE FROM USER_SDO_GEOM_METADATA;
DROP TABLE fire_buildings;
DROP TABLE buildings;
DROP TABLE hydrants;