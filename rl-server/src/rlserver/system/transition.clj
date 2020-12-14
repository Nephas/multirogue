(ns rlserver.system.transition
  (:require [rllib.board :refer [large-neighborhood]]
            [rlserver.generate.level :refer [generate-level]]
            [rllib.vector :refer [man-dist]]
            [rlserver.entity.state :refer [INITSTATE apply-seq]]))

(defn check-transition [game [id lvlid]]
  (let [transitpos (get-in game [:pos id])
        player0 (get-in game [:pos 0])
        player1 (get-in game [:pos 1])
        transit? (and (<= (man-dist player0 player1) 2)
                      (or (= transitpos player0)
                          (= transitpos player1)))]
    (if transit?
      (generate-level INITSTATE lvlid (:level game))
      game)))

(defn check-transitions [game]
  (apply-seq game (:transition game) check-transition))