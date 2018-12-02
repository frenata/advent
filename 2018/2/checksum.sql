drop schema day2 cascade;
create schema day2;

create table day2.boxes (id text);
copy day2.boxes from '/Users/frenata/src/advent/2018/2/input.txt';

create function day2.freq(inp text, search text) returns bigint as
$$
  select count(*) from regexp_matches(inp, search, 'g');
  $$ language sql;

create function day2.freqs(inp text, target int) returns bool as
$$
  declare
  found bool = false;
  c text;
  begin
    foreach c in array array['a','b','c','d','e','f','g','h','i','j','k','l','m',
                             'n','o','p','q','r','s','t','u','v','w','x','y','z'] loop
      if day2.freq(inp, c)::int = target then
        found = true;
      end if;
    end loop;
    return found;
  end; $$ language plpgsql;

create view day2.star1 as
  select
    (select count(*)
       from (select day2.freqs(id,2) from day2.boxes) as t
      where freqs = True) *
    (select count(*)
       from (select day2.freqs(id,3) from day2.boxes) as t
      where freqs = True);
