package com.campers.now;

import com.campers.now.models.Command;
import com.campers.now.models.ProductCommand;
import com.campers.now.repositories.CommandRepository;
import com.campers.now.services.Impl.CommandServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class CommandTest {

    @Mock
    private CommandRepository commandRepository;

    @InjectMocks
    private CommandServiceImpl commandService;

    @Test
     void testGetCommandById() {
        int id = 1;
        Command expectedCommand = new Command();
        expectedCommand.setId(id);

        when(commandRepository.findById(id)).thenReturn(Optional.of(expectedCommand));

        Command actualCommand = commandService.getCommandById(id);

        assertEquals(expectedCommand, actualCommand);
    }


    @Test
     void testCreateCommand() {
        Command commandToCreate = new Command();
        List<ProductCommand> productCommands = new ArrayList<>();
        commandToCreate.setProductCommands(productCommands);

        when(commandRepository.save(commandToCreate)).thenReturn(commandToCreate);

        Command result = commandService.createCommand(commandToCreate);

        assertNotNull(result);
        verify(commandRepository, times(1)).save(commandToCreate);
    }

    @Test
     void testUpdateCommand_ExistingId() {
        int id = 1;
        Command updatedCommand = new Command();

        when(commandRepository.findById(id)).thenReturn(Optional.of(new Command()));
        when(commandRepository.save(any(Command.class))).thenReturn(updatedCommand);

        Command actualCommand = commandService.updateCommand(id, new Command());

        assertEquals(updatedCommand, actualCommand);
    }

    @Test
     void testUpdateCommand_NonExistingId() {
        int id = 1;

        when(commandRepository.findById(id)).thenReturn(Optional.empty());

        Command actualCommand = commandService.updateCommand(id, new Command());

        assertEquals(null, actualCommand);
    }

    @Test
     void testDeleteCommand() {
        int id = 1;

        commandService.deleteCommand(id);

        verify(commandRepository, times(1)).deleteById(id);
    }
}

