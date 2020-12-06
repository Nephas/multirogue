(ns rlclient.ring)

(defn handler [req]
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body "Yep the server failed to find it."})