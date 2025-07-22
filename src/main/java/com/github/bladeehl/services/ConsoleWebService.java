package com.github.bladeehl.services;

import com.github.bladeehl.exceptions.TrainerNotFoundException;
import com.github.bladeehl.exceptions.UnsupportedPokemonTypeException;
import com.github.bladeehl.io.WebIO;
import com.github.bladeehl.model.ConsoleHistory;
import com.github.bladeehl.repositories.ConsoleHistoryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class ConsoleWebService {
    TrainerService trainerService;
    PokemonService pokemonService;
    BattleService battleService;
    WebIO webIO;
    ConsoleSessionState sessionState;
    ConsoleHistoryRepository historyRepository;

    public String getCurrentOutput() {
        val output = new StringBuilder();

        switch (sessionState.getState()) {
            case "trainerMenu" -> {
                output.append(webIO.getTrainerMenu());
                sessionState.setInputType("trainerMenuChoice");
            }
            case "createTrainer" -> {
                output.append(webIO.getCreateTrainerPrompt());
                sessionState.setInputType("trainerName");
            }
            case "selectTrainer" -> {
                val trainers = trainerService.getAll();

                if (trainers.isEmpty()) {
                    output.append(webIO.getNoTrainersMessage());
                    sessionState.setState("trainerMenu");
                    sessionState.setInputType("trainerMenuChoice");
                } else {
                    output.append(webIO.getSelectTrainerPrompt(trainers));
                    sessionState.setInputType("trainerIndex");
                }
            }
            case "trainerActions" -> {
                val trainer = sessionState.getTrainer();

                output.append(webIO.getTrainerActions(trainer));
                sessionState.setInputType("trainerActionChoice");
            }
            case "createPokemonType" -> {
                output.append(webIO.getCreatePokemonTypePrompt());
                sessionState.setInputType("pokemonType");
            }
            case "createPokemonName" -> {
                output.append(webIO.getCreatePokemonNamePrompt());
                sessionState.setInputType("pokemonName");
            }
            case "createPokemonHP" -> {
                output.append(webIO.getCreatePokemonHPPrompt());
                sessionState.setInputType("pokemonHP");
            }
            case "createPokemonDamage" -> {
                output.append(webIO.getCreatePokemonDamagePrompt());
                sessionState.setInputType("pokemonDamage");
            }
            case "createFirePokemonRes" -> {
                output.append(webIO.getCreateFirePokemonResPrompt());
                sessionState.setInputType("fireRes");
            }
            case "createFirePokemonPwr" -> {
                output.append(webIO.getCreateFirePokemonPwrPrompt());
                sessionState.setInputType("firePwr");
            }
            case "createWaterPokemonRes" -> {
                output.append(webIO.getCreateWaterPokemonResPrompt());
                sessionState.setInputType("waterRes");
            }
            case "createWaterPokemonPwr" -> {
                output.append(webIO.getCreateWaterPokemonPwrPrompt());
                sessionState.setInputType("waterPwr");
            }
            case "selectPokemonForBattle" -> {
                val trainer = sessionState.getTrainer();
                val pokemons = pokemonService.getByTrainer(trainer);

                if (!trainer.canBattle()) {
                    output.append(webIO.getNoPokemonForBattleMessage());
                    sessionState.setState("trainerActions");
                    output.append(webIO.getTrainerActions(trainer));
                    sessionState.setInputType("trainerActionChoice");
                } else {
                    output.append(webIO.getSelectPokemonForBattlePrompt(pokemons));
                    sessionState.setInputType("firstPokemonIndex");
                }
            }
            case "selectSecondPokemon" -> {
                val trainer = sessionState.getTrainer();
                val pokemons = pokemonService.getByTrainer(trainer);

                output.append(webIO.getSelectSecondPokemonPrompt(pokemons));
                sessionState.setInputType("secondPokemonIndex");
            }
            case "battle" -> {
                val firstPokemon = sessionState.getFirstPokemon();
                val secondPokemon = sessionState.getSecondPokemon();

                output.append(webIO.getBattleStart(firstPokemon, secondPokemon));
                output.append(webIO.getBattleTurn(battleService.getCurrentPlayablePokemon()));
                sessionState.setInputType("battleChoice");
            }
            case "updatePokemonSelect" -> {
                val trainer = sessionState.getTrainer();
                val pokemons = pokemonService.getByTrainer(trainer);

                output.append(webIO.getSelectPokemonToUpdatePrompt(pokemons));
                sessionState.setInputType("updatePokemonIndex");
            }
            case "updatePokemonField" -> {
                output.append(webIO.getUpdatePokemonFieldPrompt());
                sessionState.setInputType("updateFieldChoice");
            }
            case "updatePokemonName" -> {
                output.append(webIO.getUpdatePokemonNamePrompt());
                sessionState.setInputType("newPokemonName");
            }
            case "updatePokemonHP" -> {
                output.append(webIO.getUpdatePokemonHPPrompt());
                sessionState.setInputType("newPokemonHP");
            }
            case "updatePokemonDamage" -> {
                output.append(webIO.getUpdatePokemonDamagePrompt());
                sessionState.setInputType("newPokemonDamage");
            }
            case "deletePokemon" -> {
                val trainer = sessionState.getTrainer();
                val pokemons = pokemonService.getByTrainer(trainer);

                output.append(webIO.getDeletePokemonPrompt(pokemons));
                sessionState.setInputType("deletePokemonIndex");
            }
            case "showPokemons" -> {
                val trainer = sessionState.getTrainer();
                val pokemons = pokemonService.getByTrainer(trainer);

                output.append(webIO.getShowPokemons(pokemons));
                sessionState.setState("trainerActions");
                output.append(webIO.getTrainerActions(trainer));
                sessionState.setInputType("trainerActionChoice");
            }
        }

        return output.toString();
    }

    public String processInput(@NonNull final String input) {
        val output = new StringBuilder();
        ConsoleHistory historyEntry = ConsoleHistory.builder()
            .input(input)
            .timestamp(Instant.now())
            .build();

        try {
            switch (sessionState.getInputType()) {
                case "trainerMenuChoice" -> {
                    int choice = webIO.parseInt(input, output);
                    switch (choice) {
                        case 1 -> {
                            sessionState.setState("createTrainer");
                            sessionState.setInputType("trainerName");
                            output.append(webIO.getCreateTrainerPrompt());
                        }
                        case 2 -> {
                            sessionState.setState("selectTrainer");
                            val trainers = trainerService.getAll();

                            if (trainers.isEmpty()) {
                                output.append(webIO.getNoTrainersMessage());
                                sessionState.setState("trainerMenu");
                                sessionState.setInputType("trainerMenuChoice");
                            } else {
                                output.append(webIO.getSelectTrainerPrompt(trainers));
                                sessionState.setInputType("trainerIndex");
                            }
                        }
                        case 0 -> {
                            output.append("Выход");
                            sessionState.setState(null);
                        }
                        default -> output.append(webIO.getInvalidChoiceMessage());
                    }
                }
                case "trainerName" -> {
                    val trainer = trainerService.createTrainer(input);

                    sessionState.setTrainer(trainer);
                    sessionState.setState("trainerActions");
                    output.append("Тренер создан.\n");
                    output.append(webIO.getTrainerActions(trainer));
                    sessionState.setInputType("trainerActionChoice");
                }
                case "trainerIndex" -> {
                    try {
                        val trainer = trainerService.getTrainerByIndex(webIO.parseInt(input, output));

                        sessionState.setTrainer(trainer);
                        sessionState.setState("trainerActions");
                        output.append(webIO.getTrainerActions(trainer));
                        sessionState.setInputType("trainerActionChoice");
                    } catch (TrainerNotFoundException thrown) {
                        output.append(webIO.getInvalidChoiceMessage());
                        val trainers = trainerService.getAll();

                        output.append(webIO.getSelectTrainerPrompt(trainers));
                    }
                }
                case "trainerActionChoice" -> {
                    int choice = webIO.parseInt(input, output);
                    val trainer = sessionState.getTrainer();
                    switch (choice) {
                        case 1 -> {
                            sessionState.setState("selectPokemonForBattle");
                            val pokemons = pokemonService.getByTrainer(trainer);

                            if (!trainer.canBattle()) {
                                output.append(webIO.getNoPokemonForBattleMessage());
                                sessionState.setState("trainerActions");
                                output.append(webIO.getTrainerActions(trainer));
                                sessionState.setInputType("trainerActionChoice");
                            } else {
                                output.append(webIO.getSelectPokemonForBattlePrompt(pokemons));
                                sessionState.setInputType("firstPokemonIndex");
                            }
                        }
                        case 2 -> {
                            sessionState.setState("createPokemonType");
                            output.append(webIO.getCreatePokemonTypePrompt());
                            sessionState.setInputType("pokemonType");
                        }
                        case 3 -> {
                            sessionState.setState("updatePokemonSelect");
                            val pokemons = pokemonService.getByTrainer(trainer);

                            output.append(webIO.getSelectPokemonToUpdatePrompt(pokemons));
                            sessionState.setInputType("updatePokemonIndex");
                        }
                        case 4 -> {
                            sessionState.setState("deletePokemon");
                            val pokemons = pokemonService.getByTrainer(trainer);

                            output.append(webIO.getDeletePokemonPrompt(pokemons));
                            sessionState.setInputType("deletePokemonIndex");
                        }
                        case 5 -> {
                            sessionState.setState("showPokemons");
                            val pokemons = pokemonService.getByTrainer(trainer);

                            output.append(webIO.getShowPokemons(pokemons));
                            sessionState.setState("trainerActions");
                            output.append(webIO.getTrainerActions(trainer));
                            sessionState.setInputType("trainerActionChoice");
                        }
                        case 0 -> {
                            sessionState.setState("trainerMenu");
                            output.append(webIO.getTrainerMenu());
                            sessionState.setInputType("trainerMenuChoice");
                        }
                        default -> output.append(webIO.getInvalidChoiceMessage());
                    }
                }
                case "pokemonType" -> {
                    int type = webIO.parseInt(input, output);
                    sessionState.setPokemonType(type);
                    if (type == 1 || type == 2) {
                        sessionState.setState("createPokemonName");
                        output.append(webIO.getCreatePokemonNamePrompt());
                        sessionState.setInputType("pokemonName");
                    } else {
                        output.append(webIO.getInvalidChoiceMessage());
                        output.append(webIO.getCreatePokemonTypePrompt());
                    }
                }
                case "pokemonName" -> {
                    sessionState.setPokemonName(input);
                    sessionState.setState("createPokemonHP");
                    output.append(webIO.getCreatePokemonHPPrompt());
                    sessionState.setInputType("pokemonHP");
                }
                case "pokemonHP" -> {
                    sessionState.setPokemonHP(webIO.parseInt(input, output));
                    sessionState.setState("createPokemonDamage");
                    output.append(webIO.getCreatePokemonDamagePrompt());
                    sessionState.setInputType("pokemonDamage");
                }
                case "pokemonDamage" -> {
                    sessionState.setPokemonDamage(webIO.parseInt(input, output));
                    int type = sessionState.getPokemonType();
                    if (type == 1) {
                        sessionState.setState("createFirePokemonRes");
                        output.append(webIO.getCreateFirePokemonResPrompt());
                        sessionState.setInputType("fireRes");
                    } else {
                        sessionState.setState("createWaterPokemonRes");
                        output.append(webIO.getCreateWaterPokemonResPrompt());
                        sessionState.setInputType("waterRes");
                    }
                }
                case "fireRes" -> {
                    sessionState.setFireRes(webIO.parseInt(input, output));
                    sessionState.setState("createFirePokemonPwr");
                    output.append(webIO.getCreateFirePokemonPwrPrompt());
                    sessionState.setInputType("firePwr");
                }
                case "firePwr" -> {
                    val trainer = sessionState.getTrainer();

                    pokemonService.saveFirePokemon(
                        trainer,
                        sessionState.getPokemonName(),
                        sessionState.getPokemonHP(),
                        sessionState.getPokemonDamage(),
                        sessionState.getFireRes(),
                        webIO.parseInt(input, output));
                    sessionState.setState("trainerActions");
                    output.append(webIO.getPokemonCreatedMessage());
                    output.append(webIO.getTrainerActions(trainer));
                    sessionState.setInputType("trainerActionChoice");
                }
                case "waterRes" -> {
                    sessionState.setWaterRes(webIO.parseInt(input, output));
                    sessionState.setState("createWaterPokemonPwr");
                    output.append(webIO.getCreateWaterPokemonPwrPrompt());
                    sessionState.setInputType("waterPwr");
                }
                case "waterPwr" -> {
                    val trainer = sessionState.getTrainer();

                    pokemonService.saveWaterPokemon(
                        trainer,
                        sessionState.getPokemonName(),
                        sessionState.getPokemonHP(),
                        sessionState.getPokemonDamage(),
                        sessionState.getWaterRes(),
                        webIO.parseInt(input, output));
                    sessionState.setState("trainerActions");
                    output.append(webIO.getPokemonCreatedMessage());
                    output.append(webIO.getTrainerActions(trainer));
                    sessionState.setInputType("trainerActionChoice");
                }
                case "firstPokemonIndex" -> {
                    val trainer = sessionState.getTrainer();
                    val pokemons = pokemonService.getByTrainer(trainer);

                    int index = webIO.parseInt(input, output) - 1;
                    if (index >= 0 && index < pokemons.size()) {
                        sessionState.setFirstPokemon(pokemons.get(index));
                        sessionState.setState("selectSecondPokemon");
                        output.append(webIO.getSelectSecondPokemonPrompt(pokemons));
                        sessionState.setInputType("secondPokemonIndex");
                    } else {
                        output.append(webIO.getInvalidPokemonSelectionMessage());
                        sessionState.setState("trainerActions");
                        output.append(webIO.getTrainerActions(trainer));
                        sessionState.setInputType("trainerActionChoice");
                    }
                }
                case "secondPokemonIndex" -> {
                    val trainer = sessionState.getTrainer();
                    val pokemons = pokemonService.getByTrainer(trainer);

                    int index = webIO.parseInt(input, output) - 1;
                    val firstPokemon = sessionState.getFirstPokemon();

                    if (index >= 0 && index < pokemons.size() && !pokemons.get(index).equals(firstPokemon)) {
                        val secondPokemon = pokemons.get(index);

                        sessionState.setSecondPokemon(secondPokemon);
                        battleService.startBattle(firstPokemon, secondPokemon);
                        sessionState.setState("battle");
                        output.append(webIO.getBattleStart(firstPokemon, secondPokemon));
                        output.append(webIO.getBattleTurn(battleService.getCurrentPlayablePokemon()));
                        sessionState.setInputType("battleChoice");
                    } else {
                        output.append(webIO.getInvalidPokemonSelectionMessage());
                        sessionState.setState("trainerActions");
                        output.append(webIO.getTrainerActions(trainer));
                        sessionState.setInputType("trainerActionChoice");
                    }
                }
                case "battleChoice" -> {
                    val firstPokemon = sessionState.getFirstPokemon();
                    val secondPokemon = sessionState.getSecondPokemon();

                    int choice = webIO.parseInt(input, output);
                    switch (choice) {
                        case 1 -> {
                            val dmg = battleService.attack(
                                battleService.getCurrentPlayablePokemon(),
                                battleService.getCurrentOpponentPokemon());

                            output.append(webIO.getBattleAttack(
                                battleService.getCurrentPlayablePokemon(),
                                battleService.getCurrentOpponentPokemon(),
                                dmg));
                        }
                        case 2 -> {
                            battleService.defend(battleService.getCurrentPlayablePokemon());
                            output.append(webIO.getBattleDefend(battleService.getCurrentPlayablePokemon()));
                        }
                        case 3 -> {
                            val gain = battleService.useAbility(battleService.getCurrentPlayablePokemon());

                            output.append(webIO.getBattleAbility(battleService.getCurrentPlayablePokemon(), gain));
                        }
                        case 4 -> {
                            try {
                                val dmg = battleService.specialAttack(
                                    battleService.getCurrentPlayablePokemon(),
                                    battleService.getCurrentOpponentPokemon());
                                output.append(webIO.getBattleSpecialAttack(dmg));
                            } catch (UnsupportedPokemonTypeException thrown) {
                                output.append(webIO.getErrorMessage(thrown.getMessage()));
                            }
                        }
                        case 5 -> {
                            try {
                                battleService.defensiveAbility(battleService.getCurrentPlayablePokemon());
                                output.append(webIO.getBattleDefensiveAbility());
                            } catch (UnsupportedPokemonTypeException thrown) {
                                output.append(webIO.getErrorMessage(thrown.getMessage()));
                            }
                        }
                        case 6 -> {
                            battleService.evolve(battleService.getCurrentPlayablePokemon());
                            output.append(webIO.getBattleEvolve());
                        }
                        default -> output.append("Ошибка");
                    }
                    if (!battleService.isBattleOver()) {
                        battleService.nextTurn();
                        output.append(webIO.getBattleStatus(firstPokemon, secondPokemon));
                        output.append(webIO.getBattleTurn(battleService.getCurrentPlayablePokemon()));
                        sessionState.setInputType("battleChoice");
                    } else {
                        val winner = battleService.getWinner();

                        output.append(webIO.getBattleWinner(winner));
                        sessionState.setState("trainerActions");
                        output.append(webIO.getTrainerActions(sessionState.getTrainer()));
                        sessionState.setInputType("trainerActionChoice");
                    }
                }
                case "updatePokemonIndex" -> {
                    val trainer = sessionState.getTrainer();
                    val pokemons = pokemonService.getByTrainer(trainer);

                    int index = webIO.parseInt(input, output) - 1;
                    if (index >= 0 && index < pokemons.size()) {
                        sessionState.setSelectedPokemon(pokemons.get(index));
                        sessionState.setState("updatePokemonField");
                        output.append(webIO.getUpdatePokemonFieldPrompt());
                        sessionState.setInputType("updateFieldChoice");
                    } else {
                        output.append(webIO.getInvalidChoiceMessage());
                        output.append(webIO.getSelectPokemonToUpdatePrompt(pokemons));
                    }
                }
                case "updateFieldChoice" -> {
                    int choice = webIO.parseInt(input, output);
                    sessionState.setState(switch (choice) {
                        case 1 -> "updatePokemonName";
                        case 2 -> "updatePokemonHP";
                        case 3 -> "updatePokemonDamage";
                        default -> {
                            output.append(webIO.getInvalidChoiceMessage());
                            yield "updatePokemonField";
                        }
                    });
                    if (choice >= 1 && choice <= 3) {
                        output.append(switch (choice) {
                            case 1 -> webIO.getUpdatePokemonNamePrompt();
                            case 2 -> webIO.getUpdatePokemonHPPrompt();
                            case 3 -> webIO.getUpdatePokemonDamagePrompt();
                            default -> "";
                        });
                        sessionState.setInputType(switch (choice) {
                            case 1 -> "newPokemonName";
                            case 2 -> "newPokemonHP";
                            case 3 -> "newPokemonDamage";
                            default -> "updateFieldChoice";
                        });
                    } else {
                        output.append(webIO.getUpdatePokemonFieldPrompt());
                        sessionState.setInputType("updateFieldChoice");
                    }
                }
                case "newPokemonName" -> {
                    val pokemon = sessionState.getSelectedPokemon();

                    pokemon.setName(input);
                    pokemonService.updatePokemon(pokemon);
                    sessionState.setState("trainerActions");
                    output.append(webIO.getPokemonUpdatedMessage());
                    output.append(webIO.getTrainerActions(sessionState.getTrainer()));
                    sessionState.setInputType("trainerActionChoice");
                }
                case "newPokemonHP" -> {
                    val pokemon = sessionState.getSelectedPokemon();

                    pokemon.setHealth(webIO.parseInt(input, output));
                    pokemonService.updatePokemon(pokemon);
                    sessionState.setState("trainerActions");
                    output.append(webIO.getPokemonUpdatedMessage());
                    output.append(webIO.getTrainerActions(sessionState.getTrainer()));
                    sessionState.setInputType("trainerActionChoice");
                }
                case "newPokemonDamage" -> {
                    val pokemon = sessionState.getSelectedPokemon();

                    pokemon.setDamage(webIO.parseInt(input, output));
                    pokemonService.updatePokemon(pokemon);
                    sessionState.setState("trainerActions");
                    output.append(webIO.getPokemonUpdatedMessage());
                    output.append(webIO.getTrainerActions(sessionState.getTrainer()));
                    sessionState.setInputType("trainerActionChoice");
                }
                case "deletePokemonIndex" -> {
                    val trainer = sessionState.getTrainer();
                    val pokemons = pokemonService.getByTrainer(trainer);

                    int index = webIO.parseInt(input, output) - 1;
                    if (index >= 0 && index < pokemons.size()) {
                        pokemonService.deletePokemon(pokemons.get(index));
                        output.append(webIO.getPokemonDeletedMessage());
                        sessionState.setState("trainerActions");
                        output.append(webIO.getTrainerActions(trainer));
                        sessionState.setInputType("trainerActionChoice");
                    } else {
                        output.append(webIO.getInvalidChoiceMessage());
                        output.append(webIO.getDeletePokemonPrompt(pokemons));
                    }
                }
            }
        } catch (Exception thrown) {
            output.append(webIO.getErrorMessage(thrown.getMessage()));
        }
        historyEntry.setOutput(output.toString());
        historyRepository.save(historyEntry);

        return output.toString() + "\n[Entry ID: " + historyEntry.getId() + "]";
    }

    public List<ConsoleHistory> getHistorySince(@NonNull final Long lastEntryId) {
        return historyRepository.findByIdGreaterThanOrderByIdAsc(lastEntryId);
    }

}
