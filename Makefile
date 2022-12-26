
SRC = src
BIN = bin
EXECUTABLE = visualisateur/Visualisateur 
NOM_EXECUTABLE = visualisateur
EXT = .class
JCC = javac
CLASS = $(SRC)/**/*.java
FLAGS = -sourcepath $(SRC) -d $(BIN)


all: compilation

compilation:
	$(JCC) $(FLAGS) $(CLASS)
	@echo " "
	@echo " "
	@echo " "
	@echo "    __   ___              _ _ _                       "
	@echo "    \ \ / (_)____  _ __ _| (_) |_ _ _ __ _ _ __  ___  "
	@echo "     \ V /| (_-< || / _| | | |  _| |_/ _| | |  \/ -_) "
	@echo "      \_/ |_/__/\_|_\__|_|_|_|\__|_| \__,_|_|_|_\___| "
	@echo " "
	@echo " "
	@echo " "
	@echo " "
	@echo "     Welcome back... to Visualtrame"
	@echo " "
	@echo " "
	@echo " "
	@echo "     ...to start, use the following command : "
	@echo " "
	@echo "     java -cp bin visualisateur.Visualisateur <filenameIN> <filenameOUT>"
	@echo " "
	@echo " "
	@echo "     new update: filter can be applied after execution"
	@echo "     <filenameIN> begins with data/"
	@echo " "
	@echo " "	
clean:
	rm -rf $(BIN)/*


