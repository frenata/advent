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
  declare found bool = false;
  begin
    for c in 97..122 loop
      if day2.freq(inp, chr(c))::int = target then
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
      where freqs = True) as star1;

create function day2.distance(x text, y text) returns int as
$$
  declare dist int = 0;
  begin
    for i in 1..char_length(x) loop
      if not (substr(x, i, 1) = substr(y, i, 1)) then
        dist = dist + 1;
      end if;
    end loop;
    return dist;
  end; $$ language plpgsql;

create function day2.common(x text, y text) returns text as
$$
  declare z text = '';
  begin
    for i in 1..char_length(x) loop
      if substr(x, i, 1) = substr(y, i, 1) then
        z = z || substr(x,i,1);
      end if;
    end loop;
    return z;
  end; $$ language plpgsql;

create view day2.star2 as
  select day2.common(o.id, i.id) as star2
    from day2.boxes o,
         day2.boxes i
   where day2.distance(o.id, i.id) = 1
   limit 1;

select * from day2.star1;
select * from day2.star2;
