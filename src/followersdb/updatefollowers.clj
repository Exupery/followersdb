(ns followersdb.updatefollowers
  (:use
   [clojure.java.jdbc :as sql]
   [twitter.oauth]
   [twitter.api.restful]))

(declare update)
(declare dbwrite)

(defn -main [& args]
  (if (nil? args)
    (println "No handle specified!")
    (update (first args))))

(def oauth-creds
  (make-oauth-creds (System/getenv "TWITTER_CONSUMER_KEY")
                    (System/getenv "TWITTER_CONSUMER_SECRET")
                    (System/getenv "TWITTER_ACCESS_KEY")
                    (System/getenv "TWITTER_ACCESS_SECRET")))

(defn update [handle]
  (println (str "Updating followers of " handle))
  (def ids (followers-ids :oauth-creds oauth-creds
                          :params {:screen-name handle}))
  (if (= (:code (:status ids)) 200)
    (dbwrite (flatten (map (fn [user-ids]
                             (:body (users-lookup :oauth-creds oauth-creds
                                                  :params {:user-id (clojure.string/join "," user-ids)})))
                             (partition-all 100 (:ids (:body ids))))))
    (println (str
              "Unable to retrieve follower IDs, code: "
              (:code (:status ids)) " msg: " (:msg (:status ids)))))
  (println (str "Updating followers of " handle " complete!")))

(def db (System/getenv "DB_URL"))

(defn dbwrite [followers]
  ;TODO update unfollowers
  (dorun (map (fn [follower]
    (sql/execute! db
                  ["INSERT INTO followers SELECT ? WHERE NOT EXISTS (SELECT 1 FROM followers WHERE id=?)"
                   (:id follower)
                   (:id follower)])
    ;TODO add or update fields

    (println (:screen_name follower)) ;DELME
    (println (:name follower)) ;DELME
    (println (:location follower)) ;DELME
    (println (:created_at follower)) ;DELME
    (println (:listed_count follower)) ;DELME
    (println (:friends_count follower)) ;DELME
    (println (:followers_count follower)) ;DELME
    (println (:statuses_count follower)) ;DELME
    (println (:verified follower)) ;DELME
    (println (:utc_offset follower)));DELME
    followers)))
