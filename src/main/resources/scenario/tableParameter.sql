CREATE TABLE parameters
(
  energy double precision,
  ponderation_volume double precision,
  ponderation_difference_ext double precision,
  ponderation_volume_inter double precision,
  mindim double precision,
  maxdim double precision,
  minheight double precision,
  maxheight double precision,
  pbirth double precision,
  pdeath double precision,
  "amplitudeMaxDim" double precision,
  "amplitudeHeight" double precision,
  "amplitudeMove" double precision,
  "amplitudeRotate" double precision,
  temp double precision,
  deccoef double precision,
  "isAbsoluteNumber" boolean,
  nbiter integer,
  delta double precision,
  poisson double precision,
  id serial NOT NULL,
  CONSTRAINT prim_param PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
