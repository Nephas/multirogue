(ns rlserver.entity.state)

(def game-store (atom {}))
(def seq-store (atom 10))

(def MAP
  {:tic     0
   :full    false
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
   :effect   {}})

(def FLAGS
  {:blocking #{}
   :ai       #{}
   :pc       #{}
   :hostile  #{}})

(def INITSTATE (merge MAP COMPONENTS FLAGS))

(defn serialize-diff [id]
  (let [past (get @game-store (dec id))
        current (get @game-store id)]
    (do (swap! game-store assoc (dec id) current)
        (->> current
             (filter (fn [[k v]] (not= (get past k) v)))
             (apply concat)
             (apply hash-map)
             (str)))))

(defn serialize-full [id]
  (str (get @game-store id)))

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
