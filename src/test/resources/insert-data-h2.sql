insert into patient (id,email, password, first_name, last_name, phone_number,address,city,country,health_insurance_id,status,version)
 values (1,'Patient1@maildrop.cc', '$2a$10$ItcRjGVnH26jIqw7GRXOBeM5Wu3sGxkSdvSgTRWSjEpPno4J9T4kS', 'Milovan', 'Milic', '065258255','Marsala tita 13','Novi Sad',
 'Srbija','0625351236915','AWAITING_APPROVAL',0);
insert into patient_authority(user_id, authority_id) values (1,1);
