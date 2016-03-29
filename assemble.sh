#!/bin/sh

mkdir dissertation
cp target/dissertation.jar dissertation/
cp src/main/resources/app.properties dissertation/
cp peermanagement.sh dissertation/
tar -cvf dissertation.tar.gz dissertation
rm -rf dissertation


#Comment or remove these lines for final presentation
rm -rf ~/My_Stuff/BITS/dissertation ~/My_Stuff/BITS/dissertation.tar.gz
mv dissertation.tar.gz ~/My_Stuff/BITS/

rm -rf ~/My_Stuff/BITS/peer1 ~/My_Stuff/BITS/peer2 ~/My_Stuff/BITS/peer3

#Configure peer1
cd ~/My_Stuff/BITS/
tar -xvf dissertation.tar.gz

mv dissertation peer1
cd peer1
#mkdir watchdir

sed -i '' -e 's/peer\.name.*/peer\.name=Demo User 1/g' -e 's/peer\.tcp\.port.*/peer\.tcp\.port=9781/g' app.properties

#Configure Peer2
cd ~/My_Stuff/BITS/

tar -xvf dissertation.tar.gz

mv dissertation peer2
cd peer2
#mkdir watchdir

sed -i '' -e 's/peer\.name.*/peer\.name=Demo User 2/g' -e 's/peer\.tcp\.port.*/peer\.tcp\.port=9782/g' app.properties

#Configure Peer3 as rendevouz
cd ~/My_Stuff/BITS/

tar -xvf dissertation.tar.gz

mv dissertation peer3
cd peer3
#mkdir watchdir

sed -i '' -e 's/peer\.name.*/peer\.name=Rendezvouz User/g' -e 's/peer\.tcp\.port.*/peer\.tcp\.port=9783/g' -e 's/peer\.rendezvouz=.*/peer\.rendezvouz=true/g' app.properties
