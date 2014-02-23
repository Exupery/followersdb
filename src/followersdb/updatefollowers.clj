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

(defn update [handle]
  (println (str "Updating followers of " handle))
  (def ids (followers-ids :oauth-creds oauth-creds
                    :params {:screen-name handle}))
  (println ids) ;DELME
  ;TODO lookup ids via users-lookup
  ;TODO store in db
  (println (str "Updating followers of " handle " complete!")))
