(ns followersdb.updatefollowers
  (:use
   [twitter.oauth]
   [twitter.api.restful]))

(declare update)

(defn -main [& args]
  (if (nil? args)
    (println "No handle specified!")
    (update (first args))))

(def oauth-creds
  (make-oauth-creds (System/getenv "TWITTER_CONSUMER_KEY")
                    (System/getenv "TWITTER_CONSUMER_SECRET")
                    (System/getenv "TWITTER_ACCESS_KEY")
                    (System/getenv "TWITTER_ACCESS_SECRET")))

(defn lookup-users [user-ids]
  (:body (users-lookup :oauth-creds oauth-creds
                :params {:user-id (clojure.string/join "," user-ids)})))

(defn update [handle]
  (println (str "Updating followers of " handle))
  (def ids (followers-ids :oauth-creds oauth-creds
                          :params {:screen-name handle}))
  (if (= (:code (:status ids)) 200)
    (do
      (def followers (flatten (map lookup-users (partition-all 100 (:ids (:body ids))))))
      (println (first followers)) ;DELME
      (println (count followers)) ;DELME
      ;TODO store in db
     )
    (println (str
              "Unable to retrieve follower IDs, code: "
              (:code (:status ids)) " msg: " (:msg (:status ids)))))
  (println (str "Updating followers of " handle " complete!")))
