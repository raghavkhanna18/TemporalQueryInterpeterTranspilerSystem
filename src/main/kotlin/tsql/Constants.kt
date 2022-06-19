package tsql


object Constants {
    const val SUCCESS_EXIT_CODE = 0
    const val SYNTAX_EXIT_CODE = 100
    const val SEMANTIC_EXIT_CODE = 200
    const val RUNTIME_ERROR_CODE = -1

    const val COALESCE_BASE =
        """
           DROP TABLE IF EXISTS i_t;
CREATE TEMP TABLE i_t AS
SELECT a.artist,
       (GREATEST(a.start_time, b.start_time) + INTERVAL '1 DAY') as s,
       (a.end_time + INTERVAL '1 DAY')         as e
FROM top100artists11years a
        CROSS join top100songs11years b
where a.start_time < b.end_time
  and b.start_time < a.end_time;
CREATE TEMP TABLE i_t AS
SELECT artist, start_time as s, end_time as e FROM top100Artists5years;

DROP TABLE IF EXISTS  base_example_rec;
CREATE TEMP TABLE base_example_rec AS
WITH RECURSIVE all_rel AS (SELECT *
                           FROM i_t
                           UNION
                           SELECT *
                           FROM (WITH inner_rel AS (SELECT * FROM all_rel)
                                 SELECT c.artist,
                                        d.s as s,
                                        c.e as e
                                 from i_t c
                                          CROSS join inner_rel d
                                 where c.s <= d.e
                                   and c.e >= d.e
                                   and d.s != c.s
                                   and c.e != d.e
                                   and c.artist = d.artist)
                               t)
SELECT *
FROM all_rel;

SELECT artist,  s, e FROM (SELECT *,
                row_number() OVER
                    (PARTITION BY artist,  s ORDER BY e desc) AS srown,
                row_number() OVER
                    (PARTITION BY artist,e ORDER BY s asc) AS erown
                FROM base_example_rec) r
        WHERE  r.srown = 1 AND r.erown = 1 ORDER BY s, e desc;
        """
}
