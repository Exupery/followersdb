(ns followersdb.updatefollowers)

(defn -main [& args]
  (if (nil? args)
    (do (println "No handle specified!"))
    (do
    (println (str "Updating followers of " (first args))))))
