select to_char(rawtohex(account_oid)) from account where display_name = 'JCT'

select * from account where account_oid = 'A5C746A18583049DE0401F0A07991111'

select to_char(rawtohex(account_oid)), to_char(rawtohex(user_oid)) from account_user where account_oid = 'A523504A185B357FE0401F0A059972D7'

select to_char(rawtohex(account_oid)), to_char(rawtohex(user_oid)) from account_user where account_oid = 'A566B1B446687954E0401F0A0799587E' and username = 'npartangel2'
select * from account_user where account_oid = 'A566B1B446687954E0401F0A0799587E'

select to_char(rawtohex(node_account_id)), to_char(rawtohex(account_oid)) , to_char(rawtohex(node_oid))  from node_account where account_oid = 'A58B89E55DD85D19E0401F0A059949BB';

select to_char(rawtohex(node_account_id)) from node_account where account_oid = 'A58B89E55DBC5D19E0401F0A059949BB' and node_oid = '7777'
-------------------------------------------------------
select to_char(rawtohex(account_oid)) from account where display_name = 'javatestclient'


select to_char(rawtohex(account_oid)), to_char(rawtohex(user_oid))  from account_user where account_oid = 'A566B1B446687954E0401F0A0799587E' and username = 'npartangel2'

select to_char(rawtohex(node_account_id)), to_char(rawtohex(account_oid)) , to_char(rawtohex(node_oid))  from node_account where account_oid = 'A566B1B446687954E0401F0A0799587E';

select to_char(rawtohex(node_oid)) from node where node_id = 'urn:dece:org:org:dece:8888'

select to_char(rawtohex(node_user_id)) from node_user where user_oid = 'A566B1B4466D7954E0401F0A0799587E' and  node_oid = '7777'

select to_char(rawtohex(a.node_account_id)), to_char(rawtohex(b.node_user_id)) from node_account a, node_user b, account_user c
where a.account_oid = c.account_oid
and a.node_oid = '7777'
and b.node_oid = '7777'
and b.user_oid = c.user_oid
and c.username = 'npartang6'

select * from account_user where username='npartang6'


