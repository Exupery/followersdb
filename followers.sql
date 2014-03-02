CREATE TABLE followers (
  id BIGINT PRIMARY KEY,
  screen_name VARCHAR(32),
  name VARCHAR(32),
  location VARCHAR(128),
  utc_offset_seconds INTEGER,
  joined_twitter TIMESTAMP,
  listed INTEGER,
  following INTEGER,
  followers INTEGER,
  tweets INTEGER,
  verified BOOLEAN,
  started_following TIMESTAMP NOT NULL DEFAULT NOW(),
  stopped_following TIMESTAMP DEFAULT NULL
);

CREATE INDEX ON followers (screen_name, location);
CREATE INDEX ON followers (started_following, stopped_following);
CREATE INDEX ON followers (joined_twitter, tweets);
CREATE INDEX ON followers (followers, following);
