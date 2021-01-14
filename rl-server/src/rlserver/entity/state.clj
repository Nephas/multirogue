(ns rlserver.entity.state
  (:require [clojure.data.json :as json]
            [clojure.string :as s]))

;TODO write validator to throw IllegalStateException
(def game-store (atom {}))
(def seq-store (atom 10))

(def MAP
  {:tic     0
   :turn    0
   :load    -1
   :open    nil
   :mapsize nil
   :maphash 0
   :biome   nil
   :pap     [0 0]})

(def COMPONENTS
  {:pos      {}
   :desc     {}
   :animated {}
   :sprite   {}
   :hp       {}
   :dmg      {}
   :timer    {}
   :follow   {}})

(def FLAGS
  {:block   #{}
   :ai      #{}
   :pc      #{}
   :hostile #{}})

(def INITSTATE (merge MAP COMPONENTS FLAGS))

(defn compress [data]
  (-> (str data)
      (s/replace #"," "")
      (s/replace #"\] " "]")
      (s/replace #" \[" "[")
      (s/replace #"\} " "}")
      (s/replace #" \{" "{")))

(defn serialize-diff [id]
  (let [past (get @game-store (dec id))
        current (get @game-store id)]
    (do (swap! game-store assoc (dec id) current)
        (->> current
             (filter (fn [[k v]] (not= (get past k) v)))
             (apply concat)
             (apply hash-map)
             (compress)))))

(defn serialize-full [id]
  (str (get @game-store id)))

(defn serialize-json [id]
  (json/write-str (get @game-store id)))

