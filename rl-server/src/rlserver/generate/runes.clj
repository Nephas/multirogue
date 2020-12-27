(ns rlserver.generate.runes
  (:require [rlserver.entity.entity :refer [create-entity]]
            [rllib.board :refer [move]]))

(defn generate-runestone [state pos]
  (create-entity state
                 {:pos        pos
                  :desc       "altar"
                  :sprite     [20 3]
                  :consumable :buff}))

(defn generate-standing-stone [state pos]
  (create-entity state
                 {:pos    pos
                  :desc   "runestone"
                  :sprite (rand-nth [[8 10] [9 10] [10 10] [11 10]])}
                 [:block]))

(defn generate-blood-circle [state pos]
  (-> state
      (generate-runestone pos)
      (generate-standing-stone (-> pos (move :up) (move :left)))
      (generate-standing-stone (-> pos (move :up) (move :right)))
      (generate-standing-stone (-> pos (move :down) (move :left)))
      (generate-standing-stone (-> pos (move :down) (move :right)))))