drop schema day1 cascade;
create schema day1;

create table day1.freqs (freq text);
copy day1.freqs from '/Users/frenata/src/advent/2018/1/input.txt';

create view day1.star1 as select sum(freq::integer) as star1 from day1.freqs;

create table day1.changes (freq text);

create view day1.star2 as -- idiom for getting the first duplicate row
  select freq as star2 from day1.changes
   where freq in
         (select * from day1.changes
           group by freq
          having count(freq) > 1)
   limit 1;

create function day1.reduce(curr int) returns void as
$$
  declare
  change int;
  begin
    while (select not exists(select * from day1.star2)) loop -- key line!
      for change in (select * from day1.freqs) loop
        curr = curr + change;
        insert into day1.changes values (curr);
      end loop;
    end loop;
  end; $$ language plpgsql;

select day1.reduce(0);

select * from day1.star1;
select * from day1.star2;
