(ns database
  (:use [facts :only [is-valid-fact?]]
    :use [rules :only [is-valid-rule?]])
  )

(defn is-valid-database?
  "Devuelve true si la database es valida, false por el contrario"
  [database]
  (reduce =
          (map (fn [x] (or (is-valid-fact? x) (is-valid-rule? x))) database)))

(defn parse-raw-database
  "Recibe una base de datos con facts y rules y devuelve un set con los facts y rules parseados.

  Ejemplo base de datos:
  varon(juan).
  varon(pedro).
  hijo(X, Y) :- varon(X), padre(Y, X)."
  [raw-database]
  (set
   (filter
    (fn [x] (not (clojure.string/blank? x) ))
    (map
     (fn [string] (clojure.string/trim-newline (clojure.string/trim string)) )
     (clojure.string/split raw-database  #"\.")
     ))))

(defn generate-database
  ""
  [raw-database]
  (let [parsed-database (parse-raw-database raw-database)]
    (if (not (is-valid-database? parsed-database)) nil
      {
        :facts (filter is-valid-fact? parsed-database),
        :rules (filter is-valid-rule? parsed-database)
        }
      )))

