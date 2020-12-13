(ns rlserver.entity.state)

(def game-store (atom {}))
(def seq-store (atom 10))

(def MAP
  {:tic     0
   :open    nil
   :mapsize nil
   :mapseed 0
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
   :pc       #{}})

(def INITSTATE (merge MAP COMPONENTS FLAGS))

(defn serialize-game [id]
  (str (get @game-store id)))

(defn apply-seq [state seq reducer]
  (loop [[current & remaining] seq
         next-state state]
    (if (nil? current) next-state
                       (recur remaining
                              (reducer next-state current)))))
