(ns rlserver.system.transition
  (:require [rllib.board :refer [large-neighborhood]]
            [rlserver.generate.level.level :refer [generate-level]]
            [rllib.vector :refer [man-dist]]
            [rllib.rand :refer [rand-coll]]
            [rllib.state :refer [apply-seq]]
            [rlserver.entity.state :refer [INITSTATE]]
            [rlserver.entity.entity :refer [copy-entity]]))

(defn place-near-transition [game id origin]
  (let [[transitid _] (first (filter (fn [[_ [fromlvl tolvl]]] (= tolvl origin)) (:transition game)))
        fields (vals (large-neighborhood (get-in game [:pos transitid])))]
    (assoc-in game [:pos id] (rand-coll fields))))

(defn transit-level [game tolvl fromlvl]
  (if (= 999 tolvl)
    (assoc INITSTATE :load 1)
    (-> INITSTATE
        (generate-level tolvl fromlvl)
        (copy-entity game 0)
        (copy-entity game 1)
        (assoc :pap (:pap game))
        (place-near-transition 0 fromlvl)
        (place-near-transition 1 fromlvl))))

(defn should-transit? [game id]
  (let [get-pos #(get-in game [:pos %])
        player-at-transit? #(= (get-pos id) (get-pos %))
        dead? #(zero? (get-in game [:hp % 0]))
        close? (<= (man-dist (get-pos 0) (get-pos 1)) 2)
        ai? #(contains? (set (:ai game)) %)
        not-ai? #(not (ai? %))]
    (or (and (player-at-transit? 1) (or (dead? 0) (ai? 0)))
        (and (player-at-transit? 0) (or (dead? 1) (ai? 1)))
        (and close? (player-at-transit? 0) (not-ai? 0))
        (and close? (player-at-transit? 1) (not-ai? 1)))))

(defn check-transition [game [id [fromlvl tolvl]]]
  (if (and (some? tolvl) (should-transit? game id))
    (transit-level game tolvl fromlvl)
    game))

(defn check-transitions [game]
  (apply-seq game (:transition game) check-transition))