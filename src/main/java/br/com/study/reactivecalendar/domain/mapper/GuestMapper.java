package br.com.study.reactivecalendar.domain.mapper;

import br.com.study.reactivecalendar.domain.document.GuestType;
import br.com.study.reactivecalendar.domain.document.UserDocument;
import br.com.study.reactivecalendar.domain.dto.GuestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface GuestMapper {

    default Set<GuestDTO> toDTOSet(final List<UserDocument> documents, final Set<GuestDTO> dto){
        Set<GuestDTO> guests = new HashSet<>();
        documents.forEach(d ->dto.stream()
                    .filter(guest -> guest.email().equals(d.email()))
                    .map(GuestDTO::type).findFirst()
                            .ifPresentOrElse(type -> guests.add(toDTO(d, type)), () -> {}));
        return guests;
    }

    @Mapping(target = "userId", source = "document.id")
    GuestDTO toDTO(final UserDocument document, final GuestType type);

}
