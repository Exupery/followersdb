# FollowersDb

Maintains a database of an account's Twitter followers

## Usage

Run updatefollowers.clj passing a Twitter username as the first/only argument, e.g.:

````lein run -m followersdb.updatefollowers frostmatthew````

The following environment variables must be set:

* TWITTER_CONSUMER_KEY
* TWITTER_CONSUMER_SECRET
* TWITTER_ACCESS_KEY
* TWITTER_ACCESS_SECRET
* DB_URL

## License

MIT License (MIT)
Copyright (c) 2014 Matthew Frost
