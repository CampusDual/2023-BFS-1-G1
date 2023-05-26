package com.campusdual.ontimizeBackendPartners.model.dto.dtomapper;

import com.campusdual.ontimizeBackendPartners.model.Contact;
import com.campusdual.ontimizeBackendPartners.model.dto.ContactDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ContactMapper {

    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);

    ContactDTO toDTO(Contact contact);
    List<ContactDTO> toDTOList(List<Contact> contacts);
    Contact toEntity(ContactDTO contactdto);
}