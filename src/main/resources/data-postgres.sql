-- FOR DEMO
-- AUTHORITIES
INSERT INTO AUTHORITY (name) VALUES ('ROLE_CLINICAL_CENTRE_ADMIN');
INSERT INTO AUTHORITY (name) VALUES ('ROLE_CLINIC_ADMIN');
INSERT INTO AUTHORITY (name) VALUES ('ROLE_PATIENT');
INSERT INTO AUTHORITY (name) VALUES ('ROLE_DOCTOR');
INSERT INTO AUTHORITY (name) VALUES ('ROLE_NURSE');

-- CLINICS
insert into clinic (address, description, name,clinic_rating,version) values ('Novi sad Grbavica', 'Savremena klinika u Novom Sadu', 'Klinika mira',2.5,0);
insert into clinic (address, description, name,clinic_rating,version) values ('Novi sad Tolstojeva', 'VMA', 'Klinika',3.7,0);
insert into clinic (address, description, name,clinic_rating,version) values ('Beograd Višegradska', 'Savremena klinika u Beogradu', 'Klinika u Beogradu',4.7,0);

-- CLINICAL CENTRE ADMINISTRATORS
insert into clinical_centre_administrator (email, password, first_name, last_name, phone_number, status) values
('2st.Admin@maildrop.cc', '$2a$10$AIj9aDQkl.7XQsWgqJJXjOQ39jST2/IBndwPwmBOYLU0DBoN2fUOS', 'Goran', 'Krunic', '044123456', 'ACTIVE');
insert into clinical_centre_admin_authority (user_id, authority_id) values (1,1);

insert into clinical_centre_administrator (email, password, first_name, last_name, phone_number, status) values
('3rd.Admin@maildrop.cc', '$2a$10$mQLpvfHiya6eN6jQ6kxI2uc19aKR3ONcfa7g0mp8R8cy4q5phuTj2', 'Natasa', 'Stanojevic', '045123456', 'ACTIVE');
insert into clinical_centre_admin_authority (user_id, authority_id) values (2,1);

-- CLINIC ADMINS
insert into clinic_administrator (email, password, first_name, last_name, phone_number, status,clinic_id,last_password_reset_date) values
 ('ClinicAdmin1@maildrop.cc', '$2a$10$O2sRY6wvf0lpUu/mF5RN2u9dSW3AAzEplF4g9RpzwDOfSCFRhad6C', 'Marko', 'Marković', '064153456', 'ACTIVE',1,'2020-01-05 16:33:14');
insert into clinic_admin_authority(user_id, authority_id) values (1,2);

insert into clinic_administrator (email, password, first_name, last_name, phone_number, status,clinic_id,last_password_reset_date) values
 ('ClinicAdmin2@maildrop.cc', '$2a$10$gH1BEFXyDU3hLSwebljNxu6mRIjBlbkIA5qa1nn6midTCFD3d6wf6', 'Goran', 'Krunic', '062153466', 'ACTIVE',1,'2020-01-05 17:33:14');
insert into clinic_admin_authority(user_id, authority_id) values (2,2);

insert into clinic_administrator (email, password, first_name, last_name, phone_number, status,clinic_id,last_password_reset_date) values
 ('ClinicAdmin3@maildrop.cc', '$2a$10$XXHRDI.sCCxMPSYX579Xc.i9YP2njuczi3Zg6SN4Sjfjjp3JJ0GCa', 'Kristina', 'Radic', '064353466', 'ACTIVE',2,'2020-01-05 17:33:14');
insert into clinic_admin_authority(user_id, authority_id) values (3,2);

-- ROOMS
insert into room (label, kind,status,clinic_id,version) values ('Room 1', 'EXAMINATION','EXISTING' ,1,0);
insert into room (label, kind,status,clinic_id,version) values ('Room 2', 'EXAMINATION','EXISTING' ,1,0);
insert into room (label, kind,status,clinic_id,version) values ('Room 3', 'EXAMINATION','EXISTING' ,1,0);
insert into room (label, kind,status,clinic_id,version) values ('Room 4', 'EXAMINATION','DELETED' ,1,0);
insert into room (label, kind,status,clinic_id,version) values ('Room 5', 'OPERATION','EXISTING' ,1,0);
insert into room (label, kind,status,clinic_id,version) values ('Room 6', 'OPERATION','EXISTING' ,1,0);
insert into room (label, kind,status,clinic_id,version) values ('Room 7', 'OPERATION','DELETED' ,1,0);
insert into room (label, kind,status,clinic_id,version) values ('Room 8', 'EXAMINATION','DELETED' ,1,0);
insert into room (label, kind,status,clinic_id,version) values ('Room 9', 'OPERATION','EXISTING' ,2,0);
insert into room (label, kind,status,clinic_id,version) values ('Room 10', 'EXAMINATION','EXISTING' ,2,0);

-- EXAMINATION TYPES
insert into examination_type (label, price,status,clinic_id,version) values ('Opsta praksa', '1000','EXISTING',1,0);
insert into examination_type (label, price,status,clinic_id,version) values ('Dermatolog', '2000','EXISTING',1,0);
insert into examination_type (label, price,status,clinic_id,version) values ('Oftamolog', '1800','DELETED',1,0);
insert into examination_type (label, price,status,clinic_id,version) values ('Ginekolog', '1800','EXISTING',1,0);
insert into examination_type (label, price,status,clinic_id,version) values ('Kardiolog', '1500','EXISTING',1,0);
insert into examination_type (label, price,status,clinic_id,version) values ('Stomatolog', '1500','EXISTING',2,0);
insert into examination_type (label, price,status,clinic_id,version) values ('Psiholog', '1500','EXISTING',2,0);
insert into examination_type (label, price,status,clinic_id,version) values ('Psihijatar', '1500','EXISTING',2,0);

-- DOCTORS
insert into doctor (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status,specialized_id, doctor_rating)
 values ('ClinicDoctor1@maildrop.cc', '$2a$10$8a31x8.EmtJDWFCOnRC/KeqaKMuestVbhnoiEmnJtP6ug35gGnAXW', 'Miodrag', 'Simic', '065256856','07:30','20:00',1,'ACTIVE',1,4);
insert into doctor_authority(user_id, authority_id) values (1,4);

 insert into doctor (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status,specialized_id,doctor_rating)
 values ('ClinicDoctor2@maildrop.cc', '$2a$10$rM/W9NQ.EGYIoUJM0W0CNu6inqsiTNflYcgpkntfccx5pyg2Nkk.e', 'Stefan', 'Lazarevic', '065257165','06:00','23:30',1,'ACTIVE',1,2.5);
insert into doctor_authority(user_id, authority_id) values (2,4);

 insert into doctor (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status,specialized_id, doctor_rating)
 values ('ClinicDoctor3@maildrop.cc', '$2a$10$/xvhW3WdYpVCbIix6euixOQZ/fgHaJmYgzWoVRIk6XgtLU1wkTbfy', 'Tamara', 'Popovic', '065356856','07:30','22:00',1,'ACTIVE',2,4.2);
insert into doctor_authority(user_id, authority_id) values (3,4);

 insert into doctor (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status,specialized_id, doctor_rating)
 values ('ClinicDoctor4@maildrop.cc', '$2a$10$.qLdExRx5cV9houZHL9nYuck0PoPNnzA8Fh9XthMfTMhgqvhESVlW', 'Lazar', 'Simic', '067356856','06:30','23:00',1,'ACTIVE',2,4.3);
insert into doctor_authority(user_id, authority_id) values (4,4);

 insert into doctor (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status,specialized_id, doctor_rating)
 values ('ClinicDoctor5@maildrop.cc', '$2a$10$ghIKnGSHW.v/YFXDLExO5eiwLWJv3XWkIISTuvjqKzWlFU5XN2Uau', 'Natasa', 'Reljin', '061356856','13:30','22:00',1,'ACTIVE',3,5.0);
insert into doctor_authority(user_id, authority_id) values (5,4);

 insert into doctor (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status,specialized_id, doctor_rating)
 values ('ClinicDoctor6@maildrop.cc', '$2a$10$yQg3eaHOvUHWysGsKZwljOiT21qpaMPDtQae4LEwlR3JR7gqrclLm', 'Miodrag', 'Lakic', '061456856','09:30','15:00',2,'ACTIVE',3,5.0);
insert into doctor_authority(user_id, authority_id) values (6,4);

-- NURSES
insert into nurse (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status)
values ('Nurse.01@maildrop.cc', '$2a$10$DA4BkIosF63izoEviNbb9.PehyMoJi90XTmKlOvKCpqtrk0s4nB0y', 'Milica', 'Milić', '065432432','07:30','20:00',1,'ACTIVE');
insert into nurse_authority(user_id, authority_id) values (1,5);

insert into nurse (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status)
values ('Nurse.02@maildrop.cc', '$2a$10$6A0pH//FyYQxKmD.wUacjeSJaxewWg0JiZNEcISDSS.wwKSW2yOR.', 'Tamara', 'Simic', '065432433','02:00','23:30',1,'ACTIVE');
insert into nurse_authority(user_id, authority_id) values (2,5);

insert into nurse (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status)
values ('Nurse.03@maildrop.cc', '$2a$10$9zcv/KAujCPzByl2tBHfWeQCL6kSkS7KVengW91V9/Bz58sUimTqu', 'Nikolina', 'Aleksic', '065432533','07:00','23:30',1,'ACTIVE');
insert into nurse_authority(user_id, authority_id) values (3,5);

insert into nurse (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status)
values ('Nurse.04@maildrop.cc', '$2a$10$O99IQhdqq5Wo9QGS7/Gf6uawu4J99DZayST4pCsMDDPZ92Amh3k7y', 'Dejan', 'Stankovic', '065458533','06:00','23:30',1,'ACTIVE');
insert into nurse_authority(user_id, authority_id) values (4,5);

insert into nurse (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status)
values ('Nurse.05@maildrop.cc', '$2a$10$7VdTiokGWnxSs8Iwud3ioOEffXqexjYbjadwnSi9F2lcvlSHLg/yy', 'Jelena', 'Vlaskalic', '067788933','06:00','23:30',1,'ACTIVE');
insert into nurse_authority(user_id, authority_id) values (5,5);

insert into nurse (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status)
values ('Nurse.06@maildrop.cc', '$2a$10$utR/KCLYQe7P4Twd.me8E.OzCoBKYWONp2dyzwg4cQMAXxL51NXR6', 'Katarina', 'Petrovic', '065759833','06:00','23:30',1,'ACTIVE');
insert into nurse_authority(user_id, authority_id) values (6,5);

insert into nurse (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status)
values ('Nurse.07@maildrop.cc', '$2a$10$PSFQX8xL.enLKzfGGZ6t6ujRRVEqfCZzqcIdrlfj4IgsDyjMKS5pq', 'Jovana', 'Stankovic', '065459543','06:00','23:30',1,'ACTIVE');
insert into nurse_authority(user_id, authority_id) values (7,5);

insert into nurse (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status)
values ('Nurse.08@maildrop.cc', '$2a$10$7pIbbBV4NIVdrlCdq0gXD.vf.xZvANsjR6Km5EQGCyIBbG9B.U..6', 'Tijana', 'Stankovic', '065459573','06:00','23:30',1,'ACTIVE');
insert into nurse_authority(user_id, authority_id) values (8,5);

insert into nurse (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status)
values ('Nurse.09@maildrop.cc', '$2a$10$w4Oy2m.JPH200XrobIDjTOokWPUh1th7LDzS9j9GAS1Dh0nEckCQm', 'Bojana', 'Stamenkovic', '065459559','06:00','23:30',1,'ACTIVE');
insert into nurse_authority(user_id, authority_id) values (9,5);

insert into nurse (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status)
values ('Nurse.10@maildrop.cc', '$2a$10$NziXEL7lpKCUi9Hal4t4geIxqMuhLTasCSyF2IN0fDtEzm42nTdne', 'Dmitar', 'Pavlov', '065459643','06:00','23:30',1,'ACTIVE');
insert into nurse_authority(user_id, authority_id) values (10,5);

-- PATIENTS
 insert into patient (email, password, first_name, last_name, phone_number,address,city,country,health_insurance_id,status,version)
 values ('Patient1@maildrop.cc', '$2a$10$ESM7nrRIZUFJbxL7GOnVeOqJq2XkUGUBf0xzQnWphrPGoOhIwNs4G', 'Milovan', 'Milic', '065258255','Petra Drapsina 13','Novi Sad',
 'Srbija','1625351236915','ACTIVATED',0);
insert into patient_authority(user_id, authority_id) values (1,3);
 insert into medical_record(height, weight, blood_type, allergies, patient_id) values (176, 80, 'B-', 'Prasina, jaja', 1);

  insert into patient (email, password, first_name, last_name, phone_number,address,city,country,health_insurance_id,status,version)
 values ('Patient2@maildrop.cc', '$2a$10$blloYkIX9b1PDVBT8uOZ6ejdA2vHCYhoTSYSBToN1dq68aOKzwGF.', 'Stefan', 'Lazarevic', '065258355','Gogoljeva 42','Novi Sad',
 'Srbija','2625352236915','ACTIVATED',0);
insert into patient_authority(user_id, authority_id) values (2,3);
insert into medical_record(height, weight, blood_type, allergies, patient_id) values (165, 65, '0', 'Grinje', 2);

 insert into patient (email, password, first_name, last_name, phone_number,address,city,country,health_insurance_id,status,version)
 values ('Patient3@maildrop.cc', '$2a$10$YH6x9.Kq8v.RNI2pox6pSuwG0E7T28y9Q7Xbp86lNR0HbNY98josG', 'Marko', 'Stefanovic', '065258455','Marsala tita 43','Beograd',
 'Srbija','3625342236915','ACTIVATED',0);
insert into patient_authority(user_id, authority_id) values (3,3);
insert into medical_record(height, weight, blood_type, allergies, patient_id) values (202, 95, 'AB', 'Orasasti plodovi', 3);

insert into patient (email, password, first_name, last_name, phone_number,address,city,country,health_insurance_id,status,version)
 values ('Patient4@maildrop.cc', '$2a$10$wyempd2kjsLxS8iYb67GdOZ1HVluL4y6RabeXT6Taia9.nNLlkuBK', 'Jovana', 'Pajic', '069998877','Jovana Ducica 18','Ruma',
 'Srbija','4625372236915','ACTIVATED',0);
insert into patient_authority(user_id, authority_id) values (4,3);
insert into medical_record(height, weight, blood_type, allergies, patient_id) values (158, 52, 'A+', 'Nema alergije', 4);

-- DIAGNOSES
insert into diagnose (title, description) values ('Asthma', 'Asthma is a condition in which hyperreactive airways constrict ' ||
 'and result in symptoms like wheezing, coughing, and shortness of breath. Inhaled corticosteroids (ICS) and long-acting ' ||
  'bronchodilators (LABAs) are used in the treatment of asthma.');
insert into diagnose (title, description) values ('Diabetes', 'Diabetes is a chronic condition characterized by high levels ' ||
 'of sugar (glucose) in the blood. Symptoms of diabetes include increased urine output, thirst, hunger, and fatigue.');
insert into diagnose (title, description) values ('Diarrhea', 'Diarrhea — loose, watery and possibly more-frequent bowel movements' ||
 ' — is a common problem.');
insert into diagnose (title, description) values ('Cold', 'The common cold, also known simply as a cold, is a viral infectious ' ||
 'disease of the upper respiratory tract that primarily affects the nose.');
insert into diagnose (title, description) values ('Allergic reaction',
 'The symptoms of an allergic reaction can vary from mild to severe. If you become exposed to an allergen for the first time, ' ||
  'your symptoms may be mild. These symptoms may get worse if you repeatedly come into contact with the allergen.');


-- MEDICINES
insert into medicine (label, chemical_composition, "usage") values ('Vicodin', 'hydrocodone, acetaminophen, mauris, natoque ',
 'Vicodin is a popular drug for treating acute or chronic moderate to moderately severe pain. Its most common side effects are ' ||
 'lightheadedness, dizziness, sedation, nausea, and vomiting. Vicodin can reduce breathing, impair thinking, reduce ' ||
 'physical abilities, and is habit forming.');

insert into medicine (label, chemical_composition, "usage") values ('Lipitor', 'atorvastatin, auctor, consectetur, dapibus, facilisis',
 'Lipitor is a "statin" (HMG-CoA reductase inhibitors) approved for treating high cholesterol. It also prevents chest pain, ' ||
 'stroke, heart attack in individuals with coronary artery disease. It causes minor side effects such as constipation, ' ||
 'diarrhea, fatigue, gas, heartburn, and headache. Like other statins it can cause muscle pain and muscle break down. ');

insert into medicine (label, chemical_composition, "usage") values ('Amoxicillin', 'percipit, adipisci, urbanitas',
 'Amoxicillin is a penicillin type antibiotic used for treating several types of bacterial infections such as ear, tonsils, throat, larynx, ' ||
 'urinary tract, and skin infections. Its side effects are diarrhea, heartburn, nausea, itching, vomiting, confusion, abdominal pain, rash, ' ||
 'and allergic reactions.');

insert into medicine (label, chemical_composition, "usage") values ('Simvastatin', 'rhoncus, urna, neque, viverra',
 'Simvastatin is one of the first "statins" (HMG-CoA reductase inhibitors) approved for treating high cholesterol and reducing the risk ' ||
 'of stroke, death from heart disease, and risk of heart attacks. ');

insert into medicine (label, chemical_composition, "usage") values ('Lisinopril', 'pbibendum, enim, facilisis, gravida',
 'Lisinopril is an angiotensin converting enzyme (ACE) inhibitor used for treating high blood pressure, congestive heart failure, and for ' ||
 'preventing kidney failure caused by high blood pressure and diabetes.');

insert into medicine (label, chemical_composition, "usage") values ('Metformin', 'vitae, velit, dignissim ',
 'Metformin is used alone or in combination with other drugs for treating type 2 diabetes in adults and children.');

insert into medicine (label, chemical_composition, "usage") values ('Azithromycin', 'amet, mauris, commodo, quis, imperdiet',
 'Azithromycin is an antibiotic used for treating ear, throat, and sinus infections as well as pneumonia, bronchitis, and some sexually ' ||
 'transmitted diseases. Its common side effects include loose stools, nausea, stomach pain, and vomiting. ');

 -- EXAMINATIONS FOR BUSINESS REPORT
insert into date_time_interval (start_date_time, end_date_time) values ('07.01.2019 07:00','07.01.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',1,'APPROVED',1,1,1,1,1,1,0);
insert into examining (examination_id,doctor_id) values (1,1);

insert into date_time_interval (start_date_time, end_date_time) values ('14.01.2019 08:00','14.01.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',2,'APPROVED',1,1,1,1,2,3,0);
insert into examining (examination_id,doctor_id) values (2,1);

insert into date_time_interval (start_date_time, end_date_time) values ('22.01.2019 07:00','22.01.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',3,'APPROVED',1,1,1,1,2,4,0);
insert into examining (examination_id,doctor_id) values (3,2);

insert into date_time_interval (start_date_time, end_date_time) values ('16.01.2019 07:00','16.01.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',4,'APPROVED',1,1,1,1,2,5,0);
insert into examining (examination_id,doctor_id) values (4,2);

insert into date_time_interval (start_date_time, end_date_time) values ('26.01.2019 07:00','26.01.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',5,'APPROVED',1,1,1,1,2,6,0);
insert into examining (examination_id,doctor_id) values (5,2);

insert into date_time_interval (start_date_time, end_date_time) values ('04.02.2019 07:00','04.02.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',6,'APPROVED',1,1,1,1,2,7,0);
insert into examining (examination_id,doctor_id) values (6,2);

insert into date_time_interval (start_date_time, end_date_time) values ('15.02.2019 07:00','15.02.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',7,'APPROVED',2,1,1,1,2,8,0);
insert into examining (examination_id,doctor_id) values (7,3);

insert into date_time_interval (start_date_time, end_date_time) values ('21.02.2019 07:00','21.02.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',8,'APPROVED',2,1,1,1,2,9,0);
insert into examining (examination_id,doctor_id) values (8,3);

insert into date_time_interval (start_date_time, end_date_time) values ('15.03.2019 07:00','15.03.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',9,'APPROVED',2,1,1,1,1,10,0);
insert into examining (examination_id,doctor_id) values (9,3);

insert into date_time_interval (start_date_time, end_date_time) values ('31.03.2019 07:00','31.03.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',10,'APPROVED',2,1,1,1,2,2,0);
insert into examining (examination_id,doctor_id) values (10,4);

insert into date_time_interval (start_date_time, end_date_time) values ('24.03.2019 07:00','24.03.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',11,'APPROVED',2,1,1,1,1,2,0);
insert into examining (examination_id,doctor_id) values (11,4);

insert into date_time_interval (start_date_time, end_date_time) values ('06.03.2019 07:00','06.03.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',12,'APPROVED',2,1,1,1,2,2,0);
insert into examining (examination_id,doctor_id) values (12,4);

insert into date_time_interval (start_date_time, end_date_time) values ('06.04.2019 07:00','06.04.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',13,'APPROVED',2,1,1,1,2,2,0);
insert into examining (examination_id,doctor_id) values (13,4);

insert into date_time_interval (start_date_time, end_date_time) values ('18.04.2019 07:00','18.04.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',14,'APPROVED',1,1,1,1,1,2,0);
insert into examining (examination_id,doctor_id) values (14,1);

insert into date_time_interval (start_date_time, end_date_time) values ('18.06.2019 07:00','18.06.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',15,'APPROVED',1,1,1,1,1,2,0);
insert into examining (examination_id,doctor_id) values (15,1);

insert into date_time_interval (start_date_time, end_date_time) values ('05.06.2019 07:00','05.06.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',16,'APPROVED',1,1,1,1,1,1,0);
insert into examining (examination_id,doctor_id) values (16,1);

insert into date_time_interval (start_date_time, end_date_time) values ('09.06.2019 07:00','09.06.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',17,'APPROVED',1,1,1,1,1,2,0);
insert into examining (examination_id,doctor_id) values (17,1);

insert into date_time_interval (start_date_time, end_date_time) values ('24.06.2019 07:00','24.06.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',18,'APPROVED',1,1,1,1,1,3,0);
insert into examining (examination_id,doctor_id) values (18,1);

insert into date_time_interval (start_date_time, end_date_time) values ('26.06.2019 07:00','26.06.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',19,'APPROVED',1,1,1,1,1,4,0);
insert into examining (examination_id,doctor_id) values (19,1);

insert into date_time_interval (start_date_time, end_date_time) values ('02.07.2019 07:00','02.07.2019 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',20,'APPROVED',1,1,1,1,1,5,0);
insert into examining (examination_id,doctor_id) values (20,2);

insert into date_time_interval (start_date_time, end_date_time) values ('18.07.2019 08:00','18.07.2019 12:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',21,'APPROVED',1,1,1,1,2,6,0);
insert into examining (examination_id,doctor_id) values (21,2);

insert into date_time_interval (start_date_time, end_date_time) values ('18.09.2019 08:00','18.09.2019 12:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',22,'APPROVED',1,1,1,1,2,2,0);
insert into examining (examination_id,doctor_id) values (22,2);

insert into date_time_interval (start_date_time, end_date_time) values ('25.09.2019 08:00','25.09.2019 12:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',23,'APPROVED',1,1,1,1,2,7,0);
insert into examining (examination_id,doctor_id) values (23,2);

insert into date_time_interval (start_date_time, end_date_time) values ('25.10.2019 08:00','25.10.2019 12:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',24,'APPROVED',1,1,1,1,2,8,0);
insert into examining (examination_id,doctor_id) values (24,2);

insert into date_time_interval (start_date_time, end_date_time) values ('11.10.2019 08:00','11.10.2019 12:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',25,'APPROVED',1,1,1,1,2,8,0);
insert into examining (examination_id,doctor_id) values (25,2);

insert into date_time_interval (start_date_time, end_date_time) values ('09.10.2019 08:00','09.10.2019 12:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',26,'APPROVED',1,1,1,1,2,9,0);
insert into examining (examination_id,doctor_id) values (26,2);

insert into date_time_interval (start_date_time, end_date_time) values ('19.10.2019 08:00','19.10.2019 12:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',27,'APPROVED',1,1,1,1,2,9,0);
insert into examining (examination_id,doctor_id) values (27,2);

insert into date_time_interval (start_date_time, end_date_time) values ('19.11.2019 08:00','19.11.2019 12:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',28,'APPROVED',1,1,1,1,2,10,0);
insert into examining (examination_id,doctor_id) values (28,2);

insert into date_time_interval (start_date_time, end_date_time) values ('19.12.2019 08:00','19.12.2019 12:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',29,'APPROVED',1,1,1,1,2,2,0);
insert into examining (examination_id,doctor_id) values (29,2);

insert into date_time_interval (start_date_time, end_date_time) values ('13.12.2019 08:00','13.12.2019 12:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',30,'APPROVED',1,1,1,1,2,2,0);
insert into examining (examination_id,doctor_id) values (30,2);

insert into date_time_interval (start_date_time, end_date_time) values ('10.12.2019 08:00','10.12.2019 12:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',31,'APPROVED',1,1,1,1,2,2,0);
insert into examining (examination_id,doctor_id) values (31,2);

-- EXAMINATION REPORT AND PRESCRIPTION
insert into examination_report(comment, time_created, diagnose_id, doctor_id, medical_record_id, examination_id)
 values ('Pacijent ima astmu', '07.01.2019. 07:30', 1, 1, 1, 1);

insert into prescription(status, examination_report_id, medicine_id, nurse_id)
 values ('STAMPED', 1, 1, 1);
insert into prescription(status, examination_report_id, medicine_id, nurse_id)
 values ('STAMPED', 1, 2, 1);
insert into prescription(status, examination_report_id, medicine_id, nurse_id)
 values ('STAMPED', 1, 5, 1);
insert into prescription(status, examination_report_id, medicine_id, nurse_id)
 values ('STAMPED', 1, 7,1);
 insert into prescription(status, examination_report_id, medicine_id, nurse_id)
 values ('STAMPED', 1, 3, 1);
 insert into prescription(status, examination_report_id, medicine_id, nurse_id)
 values ('STAMPED', 1, 4, 1);
 insert into prescription(status, examination_report_id, medicine_id, nurse_id)
 values ('STAMPED', 1, 6, 1);

 insert into examination_report(comment, time_created, diagnose_id, doctor_id, medical_record_id, examination_id)
 values ('Pacijent ima prehladu', '14.01.2019. 08:30', 4, 1, 2, 2);

insert into prescription(status, examination_report_id, medicine_id, nurse_id)
 values ('STAMPED', 2, 1, 3);
insert into prescription(status, examination_report_id, medicine_id, nurse_id)
 values ('STAMPED', 2, 2, 3);

insert into examination_report(comment, time_created, diagnose_id, doctor_id, medical_record_id, examination_id)
 values ('Pacijent ima alergijsku reakciju', '15.02.2019. 08:30', 5, 3, 2, 7);

insert into prescription(status, examination_report_id, medicine_id, nurse_id)
 values ('STAMPED', 3, 2, 8);

 insert into examination_report(comment, time_created, diagnose_id, doctor_id, medical_record_id, examination_id)
 values ('Pacijent ima prehladu', '18.04.2019. 09:30', 4, 1, 1, 14);

insert into prescription(status, examination_report_id, medicine_id, nurse_id)
 values ('STAMPED', 4, 7, 2);

 insert into prescription(status, examination_report_id, medicine_id, nurse_id)
 values ('STAMPED', 4, 6, 2);

  insert into examination_report(comment, time_created, diagnose_id, doctor_id, medical_record_id, examination_id)
 values ('Pacijent ima astmu', '18.09.2019. 09:30', 1, 2, 2, 22);

insert into prescription(status, examination_report_id, medicine_id, nurse_id)
 values ('STAMPED', 5, 5, 2);

 insert into prescription(status, examination_report_id, medicine_id, nurse_id)
 values ('STAMPED', 5, 6, 2);

-- TIME-OFFS DOCTOR
insert into date_time_interval (start_date_time, end_date_time) values ('20.03.2020 09:00','28.03.2020 17:00');
insert into time_off_doctor (status, type, doctor_id, interval_id)
 values ('REJECTED', 'HOLIDAY', 1, 32);
insert into time_off_doctor (status, type, doctor_id, interval_id)
 values ('APPROVED', 'TIME_OFF', 2, 32);
insert into time_off_doctor (status, type, doctor_id, interval_id)
 values ('AWAITING', 'TIME_OFF', 3, 32);

 insert into date_time_interval (start_date_time, end_date_time) values ('20.10.2020 09:00','20.10.2020 17:00');
 insert into time_off_doctor (status, type, doctor_id, interval_id)
 values ('AWAITING', 'TIME_OFF', 3, 33);

 insert into date_time_interval (start_date_time, end_date_time) values ('22.11.2020 09:00','28.11.2020 17:00');
 insert into time_off_doctor (status, type, doctor_id, interval_id)
 values ('AWAITING', 'TIME_OFF', 1, 34);

-- TIME-OFFS NURSES
  insert into date_time_interval (start_date_time, end_date_time) values ('24.03.2019 09:30','26.03.2019 13:30');
insert into time_off_nurse (status, type, nurse_id, interval_id)
 values ('APPROVED', 'HOLIDAY', 1, 35);
 insert into time_off_nurse (status, type, nurse_id, interval_id)
 values ('REJECTED', 'HOLIDAY', 2, 35);

 insert into date_time_interval (start_date_time, end_date_time) values ('22.07.2020 09:00','28.07.2020 17:00');
 insert into time_off_nurse (status, type, nurse_id, interval_id)
 values ('AWAITING', 'TIME_OFF', 2, 36);

  insert into date_time_interval (start_date_time, end_date_time) values ('02.08.2020 09:00','18.08.2020 17:00');
 insert into time_off_nurse (status, type, nurse_id, interval_id)
 values ('AWAITING', 'HOLIDAY', 3, 37);

   insert into date_time_interval (start_date_time, end_date_time) values ('02.08.2020 09:00','10.08.2020 17:00');
 insert into time_off_nurse (status, type, nurse_id, interval_id)
 values ('AWAITING', 'TIME_OFF', 4, 38);

-- EXAMINATIONS FOR CHANGING TIME WHEN ASSIGNING ROOM
 insert into date_time_interval (start_date_time, end_date_time) values ('19.05.2020 09:00','19.05.2020 10:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',39,'APPROVED',1,1,1,1,1,1,0);
insert into examining (examination_id,doctor_id) values (32,1);

 insert into date_time_interval (start_date_time, end_date_time) values ('19.05.2020 08:00','19.05.2020 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',40,'APPROVED',1,1,1,2,2,2,0);
insert into examining (examination_id,doctor_id) values (33,2);

 insert into date_time_interval (start_date_time, end_date_time) values ('19.05.2020 08:30','19.05.2020 09:30');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',41,'APPROVED',2,1,1,3,3,3,0);
insert into examining (examination_id,doctor_id) values (34,3);

 insert into date_time_interval (start_date_time, end_date_time) values ('19.05.2020 07:30','19.05.2020 09:15');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,patient_id, version)
 values ('EXAMINATION',42,'AWAITING',2,1,1,4,0);
insert into examining (examination_id,doctor_id) values (35,4);

-- OPERATIONS FOR CHANGING TIME WHEN ASSIGNING ROOM
 insert into date_time_interval (start_date_time, end_date_time) values ('21.05.2020 09:00','21.05.2020 10:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id, version)
 values ('OPERATION',43,'APPROVED',1,1,1,5,2,0);
insert into examining (examination_id,doctor_id) values (36,1);

 insert into date_time_interval (start_date_time, end_date_time) values ('21.05.2020 08:00','21.05.2020 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id, version)
 values ('OPERATION',44,'APPROVED',1,1,1,6,3,0);
insert into examining (examination_id,doctor_id) values (37,2);

 insert into date_time_interval (start_date_time, end_date_time) values ('21.05.2020 08:30','21.05.2020 09:30');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,patient_id, version)
 values ('OPERATION',45,'AWAITING',2,1,1,4,0);
 -- examination_id == 38

 -- EXAMINATIONS FOR CHANGING DOCTOR WHEN ASSIGNING ROOM
 insert into date_time_interval (start_date_time, end_date_time) values ('23.05.2020 09:00','23.05.2020 10:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',46,'APPROVED',1,1,1,1,4,1,0);
insert into examining (examination_id,doctor_id) values (39,1);

 insert into date_time_interval (start_date_time, end_date_time) values ('23.05.2020 08:00','23.05.2020 11:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',47,'APPROVED',1,1,1,2,3,2,0);
insert into examining (examination_id,doctor_id) values (40,2);

 insert into date_time_interval (start_date_time, end_date_time) values ('23.05.2020 08:30','23.05.2020 09:30');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',48,'APPROVED',2,1,1,3,2,3,0);
insert into examining (examination_id,doctor_id) values (41,3);

 insert into date_time_interval (start_date_time, end_date_time) values ('23.05.2020 10:00','23.05.2020 10:30');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',49,'APPROVED',2,1,1,1,1,5,0);
insert into examining (examination_id,doctor_id) values (42,4);

 insert into date_time_interval (start_date_time, end_date_time) values ('23.05.2020 07:30','23.05.2020 09:15');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,patient_id, version)
 values ('EXAMINATION',50,'AWAITING',2,1,1,1,0);
insert into examining (examination_id,doctor_id) values (43,4);

-- EXAMINATION FOR ASSIGNING ROOM WITHOUT TIME CHANGE
 insert into date_time_interval (start_date_time, end_date_time) values ('01.06.2020 11:30','01.06.2020 12:15');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,patient_id, version)
 values ('EXAMINATION',51,'AWAITING',2,1,1,1,0);
insert into examining (examination_id,doctor_id) values (44,3);

-- OPERATION FOR ASSIGNING ROOM WITHOUT TIME CHANGE
 insert into date_time_interval (start_date_time, end_date_time) values ('01.06.2020 15:00','01.06.2020 16:30');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,patient_id, version)
 values ('OPERATION',52,'AWAITING',1,1,1,3,0);
 -- examination_id == 45

-- EXAMINATION FOR TODAY
 insert into date_time_interval (start_date_time, end_date_time) values ('06.02.2020 16:30','06.02.2020 17:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id,nurse_id, version)
 values ('EXAMINATION',53,'APPROVED',1,1,1,2,2,1,0);
insert into examining (examination_id,doctor_id) values (46,1);

-- OPERATION FOR TODAY
 insert into date_time_interval (start_date_time, end_date_time) values ('06.02.2020 16:00','06.02.2020 17:30');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,room_id,patient_id, version)
 values ('OPERATION',54,'APPROVED',2,1,1,5,1,0);
insert into examining (examination_id,doctor_id) values (47,3);
insert into examining (examination_id,doctor_id) values (47,4);

/*
-- FOR TESTS
INSERT INTO AUTHORITY (name) VALUES ('ROLE_CLINICAL_CENTRE_ADMIN');
INSERT INTO AUTHORITY (name) VALUES ('ROLE_CLINIC_ADMIN');
INSERT INTO AUTHORITY (name) VALUES ('ROLE_PATIENT');
INSERT INTO AUTHORITY (name) VALUES ('ROLE_DOCTOR');
INSERT INTO AUTHORITY (name) VALUES ('ROLE_NURSE');

insert into clinic (address, description, name,clinic_rating,version) values ('Novi sad Grbavica', 'Savremena klinika u Novom Sadu', 'Klinika mira',2.5,0);
insert into clinic (address, description, name,clinic_rating,version) values ('Novi sad Tolstojeva', 'VMA', 'Klinika',3.7,0);

insert into clinic_administrator (email, password, first_name, last_name, phone_number, status,clinic_id,last_password_reset_date) values
 ('ClinicAdmin1@maildrop.cc', '$2a$10$O2sRY6wvf0lpUu/mF5RN2u9dSW3AAzEplF4g9RpzwDOfSCFRhad6C', 'Marko', 'Marković', '064153456', 'ACTIVE',1,'2020-01-05 16:33:14');
insert into clinic_admin_authority(user_id, authority_id) values (1,2);

insert into room (label, kind,status,clinic_id,version) values ('Room 1', 'EXAMINATION','EXISTING' ,1,0);
insert into room (label, kind,status,clinic_id,version) values ('Room 2', 'EXAMINATION','EXISTING' ,1,0);

insert into examination_type (label, price,status,clinic_id,version) values ('Opsta praksa', '1000','EXISTING' ,1,0);
insert into examination_type (label, price,status,clinic_id,version) values ('Dermatolog', '2000','EXISTING',1,0);
insert into examination_type (label, price,status,clinic_id,version) values ('Oftamolog', '1800','DELETED',1,0);
insert into examination_type (label, price,status,clinic_id,version) values ('Zubar', '1500','EXISTING',2,0);

 insert into doctor (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status,specialized_id, doctor_rating)
 values ('ClinicDoctor1@maildrop.cc', '$2a$10$E1K7MZ9u5TkTZtESLfc8lOulf9/aPXKXhsFxtZQYMAeRnk9ezZ42q', 'Miodrag', 'Simic', '065256856','07:30','20:00',1,'ACTIVE',1,4);
insert into doctor_authority(user_id, authority_id) values (1,4);

 insert into doctor (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status,specialized_id,doctor_rating)
 values ('ClinicDoctor2@maildrop.cc', '$2a$10$0X7DgfIVxh1.MJoKeIp6juCEkrfYplqYrnUI7oB/LyElZ7dSZ2Z1a', 'Miodrag', 'Lazarevic', '065257165','06:01','23:30',1,'ACTIVE',2,2.5);
insert into doctor_authority(user_id, authority_id) values (2,4);

 insert into doctor (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status,specialized_id, doctor_rating)
 values ('ClinicDoctor3@maildrop.cc', '$2a$10$E1K7MZ9u5TkTZtESLfc8lOulf9/aPXKXhsFxtZQYMAeRnk9ezZ42q', 'Misa', 'Popovic', '065356856','07:30','20:00',1,'ACTIVE',2,4);
insert into doctor_authority(user_id, authority_id) values (3,4);

insert into nurse (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status)
values ('Nurse.01@maildrop.cc', '$2a$10$dT.7xoRoI338DdVr0E19EOj4/xrYSFyRLF6CAWphlBEONNnX22WfK', 'Milica', 'Milić', '065432432','07:30','20:00',1,'ACTIVE');
insert into nurse_authority(user_id, authority_id) values (1,5);

insert into nurse (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status)
values ('Nurse.02@maildrop.cc', '$2a$10$dT.7xoRoI338DdVr0E19EOj4/xrYSFyRLF6CAWphlBEONNnX22WfK', 'Tamara', 'Simic', '065432433','02:01','23:30',1,'ACTIVE');
insert into nurse_authority(user_id, authority_id) values (2,5);

insert into nurse (email, password, first_name, last_name, phone_number,work_hours_from,work_hours_to,clinic_id,status)
values ('Nurse.03@maildrop.cc', '$2a$10$dT.7xoRoI338DdVr0E19EOj4/xrYSFyRLF6CAWphlBEONNnX22WfK', 'Jelena', 'Simic', '075432433','07:01','23:30',1,'ACTIVE');
insert into nurse_authority(user_id, authority_id) values (3,5);

insert into date_time_interval (start_date_time, end_date_time) values ( '07.05.2020 08:00','15.05.2020 10:00:00');
insert into time_off_doctor (status, type, doctor_id, interval_id,version)
 values ('APPROVED', 'HOLIDAY', 1, 1,0);

  insert into time_off_doctor (status, type, doctor_id, interval_id,version)
 values ('AWAITING', 'HOLIDAY', 2, 1,0);

   insert into time_off_doctor (status, type, doctor_id, interval_id,version)
 values ('AWAITING', 'TIME_OFF', 3, 1,0);

insert into time_off_nurse (status, type, nurse_id, interval_id,version)
 values ('APPROVED', 'HOLIDAY', 1, 1,0);

 insert into time_off_nurse (status, type, nurse_id, interval_id,version)
 values ('AWAITING', 'HOLIDAY', 2, 1,0);

  insert into time_off_nurse (status, type, nurse_id, interval_id,version)
 values ('AWAITING', 'HOLIDAY', 3, 1,0);

insert into patient (email, password, first_name, last_name, phone_number,address,city,country,health_insurance_id,status,version)
 values ('Patient1@maildrop.cc', '$2a$10$ItcRjGVnH26jIqw7GRXOBeM5Wu3sGxkSdvSgTRWSjEpPno4J9T4kS', 'Milovan', 'Milic', '065258255','Marsala tita 13','Novi Sad',
 'Srbija','0625351236915','AWAITING_APPROVAL',0);
insert into patient_authority(user_id, authority_id) values (1,1);

insert into patient (email, password, first_name, last_name, phone_number,address,city,country,health_insurance_id,status,version)
 values ('Patient2@maildrop.cc', '$2a$10$ItcRjGVnH26jIqw7GRXOBeM5Wu3sGxkSdvSgTRWSjEpPno4J9T4kS', 'Adam', 'Bah', '065298255','Marsala tita 14','Beograd',
 'Srbija','0625351286915','AWAITING_APPROVAL',0);
insert into patient_authority(user_id, authority_id) values (1,2);

insert into patient (email, password, first_name, last_name, phone_number,address,city,country,health_insurance_id,status,version)
 values ('Patient3@maildrop.cc', '$2a$10$ItcRjGVnH26jIqw7GRXOBeM5Wu3sGxkSdvSgTRWSjEpPno4J9T4kS', 'Srdjan', 'Stanojevic', '065898255','Marsala tita 17','Kikinda',
 'Srbija','0625351286815','ACTIVATED',0);
insert into patient_authority(user_id, authority_id) values (1,3);

insert into date_time_interval (start_date_time, end_date_time) values ('20.06.2021 09:30:00', '20.06.2021 10:30:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,patient_id,room_id,nurse_id,version)
 values ('EXAMINATION',2,'APPROVED',1,1,1,3,1,1,0);
insert into examining (examination_id,doctor_id) values (1,1);

insert into date_time_interval (start_date_time, end_date_time) values ('20.06.2021 09:45:00','20.06.2021 10:10:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,version)
 values ('EXAMINATION',3,'AWAITING',2,1,1,0);
insert into examining (examination_id,doctor_id) values (2,2);

 insert into date_time_interval (start_date_time, end_date_time) values ('20.06.2021 10:00:00', '20.06.2021 11:45:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,patient_id,room_id,nurse_id,version)
 values ('EXAMINATION',4,'APPROVED',2,1,1,3,2,2,0);
insert into examining (examination_id,doctor_id) values (3,2);


insert into date_time_interval (start_date_time, end_date_time) values ('21.07.2021 10:30:00', '21.07.2021 12:30:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,patient_id,room_id,nurse_id,version)
 values ('EXAMINATION',5,'APPROVED',1,1,1,3,1,1,0);
insert into examining (examination_id,doctor_id) values (4,1);

insert into date_time_interval (start_date_time, end_date_time) values ('21.07.2021 11:00:00', '21.07.2021 13:30:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,version)
 values ('EXAMINATION',6,'AWAITING',2,1,1,0);
insert into examining (examination_id,doctor_id) values (5,2);

 insert into date_time_interval (start_date_time, end_date_time) values ('21.07.2021 12:45:00', '21.07.2021 13:45:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,patient_id,room_id,nurse_id,version)
 values ('EXAMINATION',7,'APPROVED',1,1,1,3,2,2,0);
insert into examining (examination_id,doctor_id) values (6,1);


insert into date_time_interval (start_date_time, end_date_time) values ('21.08.2021 12:45:00', '21.08.2021 13:45:00');
insert into examination (kind,interval_id,status,examination_type_id,clinic_id,clinic_administrator_id,version)
 values ('EXAMINATION',8,'AWAITING',1,1,1,0);
insert into examining (examination_id,doctor_id) values (7,1);
*/