(defproject followersdb "0.1"
  :description "Store followers of a Twitter account"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/java.jdbc "0.3.3"]
                 [org.postgresql/postgresql "9.3-1100-jdbc41"]
                 [twitter-api "0.7.5"]])
