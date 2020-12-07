(ns rlserver.state)

(def game-store (atom {}))
(def seq-store (atom 10))

(def init-state {:tic      0

                 :entities (list 0 1)
                 :blocking (list 0 1)
                 :players  (list 0 1)

                 :pos      {0 [5 5]
                            1 [6 6]}

                 :animated {0 0
                            1 1}

                 :npc      nil
                 :open     nil
                 :mapsize  nil})

(defn serialize-game [id]
  (str (get @game-store id)))
