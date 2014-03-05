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

(defn unfollowers [followers]
  (remove nil? (let [current (set (map (fn [follower] (:id follower)) followers))]
    (sql/query db
               ["SELECT id FROM followers WHERE stopped_following IS NULL"]
               :row-fn (fn [row]
                         (if (false? (contains? current (:id row)))
                           (:id row)))))))

(defn dbwrite [followers]
  (dorun (map (fn [unfollower]
    (sql/execute! db
                  ["UPDATE followers SET stopped_following=NOW() WHERE id=?"
                   unfollower])) (unfollowers followers)))
  (dorun (map (fn [follower]
    (sql/execute! db
                  ["INSERT INTO followers SELECT ? WHERE NOT EXISTS (SELECT 1 FROM followers WHERE id=?)"
                   (:id follower)
                   (:id follower)])
    (sql/update! db :followers {:screen_name (:screen_name follower)
                                :name (:name follower)
                                :location (:location follower)
                                ;TODO parse join date
                                ;:joined_twitter (:created_at follower)
                                :listed (:listed_count follower)
                                :following (:friends_count follower)
                                :followers (:followers_count follower)
                                :tweets (:statuses_count follower)
                                :verified (:verified follower)
                                :utc_offset_seconds (:utc_offset follower)
                                } ["id=?" (:id follower)])) followers)))
