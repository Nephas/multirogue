(ns rlserver.state)

(def store (atom {}))

(defn rect [[x1 y1] [x2 y2]]
  (for [x (range x1 x2) y (range y1 y2)] [x y]))

(def init-state {:tic      0

                 :entities (list 0 1 2 3)
                 :blocking (list 0 1 2 3)
                 :players  (list 0 1)

                 :pos      {0 [4 5]
                            1 [7 5]
                            2 [6 6]
                            3 [5 7]}

                 :npc      (list 2 3)

                 :animated {0 0
                            1 1
                            2 2
                            3 2}

                 :open     (distinct (concat (rect [2 2] [8 8])
                                             (rect [10 2] [16 8])
                                             (rect [5 5] [12 12])))})

(defn serialize-game [id]
  (str (get @store id)))
