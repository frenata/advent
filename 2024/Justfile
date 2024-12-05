build:
	devcontainer build --workspace-folder .

up:
	devcontainer up \
		--mount "type=bind,source=$HOME/.config/nvim,target=/home/${USER}/.config/nvim" \
		--mount "type=bind,source=$HOME/.gitconfig,target=/home/${USER}/.gitconfig" \
		--mount "type=bind,source=$HOME/.local/share/atuin,target=/home/${USER}/.local/share/atuin" \
		--mount "type=bind,source=$HOME/.local/share/nvim,target=/home/${USER}/.local/share/nvim" \
		--mount type=bind,source=$SSH_AUTH_SOCK,target=/ssh-agent \
		--workspace-folder . --remove-existing-container true

enter:
	devcontainer exec --workspace-folder . bash

start: build up enter
