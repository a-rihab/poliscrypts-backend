package com.poliscrypts.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.poliscrypts.dto.ContactDto;
import com.poliscrypts.exception.GlobalException;
import com.poliscrypts.model.Contact;
import com.poliscrypts.model.Enterprise;
import com.poliscrypts.repository.EnterpriseRepository;

@Component
public class ContactMapper extends CommonMapper<Contact, ContactDto> {

	@Autowired
	private EnterpriseRepository enterpriseRepository;

	@Override
	protected ContactDto mapEntityToDto(Contact contact) {

		ContactDto contactDto = new ContactDto();
		contactDto.setId(contact.getId());
		contactDto.setFirstName(contact.getFirstName());
		contactDto.setLastName(contact.getLastName());
		contactDto.setType(contact.getType());
		contactDto.setAddress(contact.getAddress());
		contactDto.setTva(contact.getTva());
		contactDto
				.setEnterprises(contact.getEnterprises().stream().map(ent -> ent.getId()).collect(Collectors.toSet()));

		return contactDto;
	}

	@Override
	protected Contact mapDtoToEntity(ContactDto contactDto) {

		Contact contact = new Contact();
		contact.setId(contactDto.getId());
		contact.setFirstName(contactDto.getFirstName());
		contact.setLastName(contactDto.getLastName());
		contact.setType(contactDto.getType());
		contact.setAddress(contactDto.getAddress());
		contact.setTva(contactDto.getTva());
		Set<Enterprise> enterprises = new HashSet<>();

		for (Long id : contactDto.getEnterprises()) {
			Enterprise enterprise = enterpriseRepository.findById(id)
					.orElseThrow(() -> new GlobalException("Impossible to create contact with entreprise id " + id));

			enterprises.add(enterprise);
		}

		contact.setEnterprises(enterprises);

		return contact;
	}

}
