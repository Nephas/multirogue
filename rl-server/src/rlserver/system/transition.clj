(ns rlserver.system.transition
  (:require [rllib.board :refer [large-neighborhood]]
            [rlserver.generate.level :refer [generate-level]]
            [rllib.vector :refer [man-dist]]
            [rllib.rand :refer [rand-coll]]
            [rlserver.entity.state :refer [INITSTATE apply-seq]]
            [rlserver.entity.entity :refer [copy-entity]]))

(defn place-near-transition [game id origin]
  (let [[transitid _] (first (filter (fn [[_ [fromlvl tolvl]]] (= tolvl origin)) (:transition game)))
        fields (vals (large-neighborhood (get-in game [:pos transitid])))]
    (assoc-in game [:pos id] (rand-coll fields))))

(defn transit-level [game tolvl fromlvl]
  (-> INITSTATE
      (generate-level tolvl fromlvl)
      (copy-entity game 0)
      (copy-entity game 1)
      (place-near-transition 0 fromlvl)
      (place-near-transition 1 fromlvl)))

(defn check-transition [game [id [fromlvl tolvl]]]
  (let [transitpos (get-in game [:pos id])
        player0 (get-in game [:pos 0])
        player1 (get-in game [:pos 1])
        transit? (and (<= (man-dist player0 player1) 2)
                      (or (and (= transitpos player0) (not (contains? (set (:ai game)) 0)))
                          (and (= transitpos player1) (not (contains? (set (:ai game)) 1)))))]
    (if transit?
      (transit-level game tolvl fromlvl)
      game)))

(defn check-transitions [game]
  (apply-seq game (:transition game) check-transition))