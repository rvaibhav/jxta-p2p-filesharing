#!/bin/sh

mkdir dissertation
cp target/dissertation.jar dissertation/
cp src/main/resources/app.properties dissertation/
cp peermanagement.sh dissertation/
tar -cvf dissertation.tar.gz dissertation
rm -rf dissertation


#Comment or remove these lines for final presentation
rm -rf ~/p2p ~/p2p/dissertation.tar.gz
mkdir ~/p2p
mv dissertation.tar.gz ~/p2p/

rm -rf ~/p2p/peer1 ~/p2p/peer2 ~/p2p/peer3

#Configure peer1
cd ~/p2p
tar -xvf dissertation.tar.gz

mv dissertation peer1
cd peer1
mkdir watchdir

sed -i '' -e 's/peer\.name.*/peer\.name=Demo User 1/g' -e 's/peer\.tcp\.port.*/peer\.tcp\.port=9781/g' app.properties

#Configure Peer2
cd ~/p2p/

tar -xvf dissertation.tar.gz

mv dissertation peer2
cd peer2
mkdir watchdir

sed -i '' -e 's/peer\.name.*/peer\.name=Demo User 2/g' -e 's/peer\.tcp\.port.*/peer\.tcp\.port=9782/g' app.properties

#Configure Peer3 as rendevouz
cd ~/p2p/

tar -xvf dissertation.tar.gz

mv dissertation peer3
cd peer3
mkdir watchdir

sed -i '' -e 's/peer\.name.*/peer\.name=Rendezvouz User/g' -e 's/peer\.tcp\.port.*/peer\.tcp\.port=9783/g' -e 's/peer\.rendezvouz=.*/peer\.rendezvouz=true/g' app.properties
