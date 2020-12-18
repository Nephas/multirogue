(ns rlserver.entity.state
  (:require [clojure.data.json :as json]
            [clojure.data.codec.base64 :as b64]
            [clojure.string :as s]))

(def game-store (atom {}))
(def seq-store (atom 10))

(def MAP
  {:tic     0
   :open    nil
   :mapsize nil
   :maphash 0
   :biome   nil})

(def COMPONENTS
  {:pos      {}
   :animated {}
   :sprite   {}
   :hp       {}
   :ap       {}
   :dmg      {}
   :effect   {}
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

(defn apply-seq [state seq reducer]
  (loop [[current & remaining] seq
         next-state state]
    (if (nil? current) next-state
                       (recur remaining
                              (reducer next-state current)))))

(defn apply-times [state n reducer]
  (loop [current n
         next-state state]
    (if (zero? current) next-state
                        (recur (dec current)
                               (reducer next-state)))))
