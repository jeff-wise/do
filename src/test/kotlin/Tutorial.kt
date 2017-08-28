
import effect.Just
import effect.Maybe
import effect.Nothing
import effect.apply
import io.kotlintest.specs.StringSpec




class TutorialTests : StringSpec()
{


    init
    {

        "Simple Example #1" {

            // Read some file. Could fail for many reasons e.g. doesn't exist, no read privileges
            fun readFile() : Maybe<String> = Nothing()

            fun writeFile(fileString : String) { }

            // Parse a command from the user. Could fail if command is typed incorrectly.
            fun parseUserCommand() : Maybe<FileCommand> = Just(DeleteLine(4))

            // Given a file string and a command, perform an edit operation to the file. Note: The
            // parameters cannot be null.
            fun modifyFile(fileString : String, command : FileCommand) : String = ""

            // Apply the modify function to inputs which may or may not exist. This implies that the
            // result may or may not exist. We will have to check that, but we do not have to
            // verify if the parameters exist. If any parameter is missing, the function will fail.
            val newFileString : Maybe<String> = apply(::modifyFile, readFile(), parseUserCommand())

            when (newFileString) {
                // A result exists, we can access it with .value
                is Just    -> writeFile(newFileString.value)
                // No result exists, one of the inputs was Nothing. Note that we don't know which
                // one. For better error messages, the Eff type is more useful.
                is Nothing -> System.out.println("An error occurred.")
            }
        }

    }

}



// ---------------------------------------------------------------------------------------------
// TEST DATA TYPES
// ---------------------------------------------------------------------------------------------

// Simple Example #1
// ---------------------------------------------------------------------------------------------

sealed class FileCommand

class DeleteFile() : FileCommand()

class DeleteLine(val lineNumber : Int) : FileCommand()



