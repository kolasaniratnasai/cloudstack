// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package com.cloud.network;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cloud.api.commands.CreateNetworkCmd;
import com.cloud.api.commands.ListNetworksCmd;
import com.cloud.api.commands.ListTrafficTypeImplementorsCmd;
import com.cloud.api.commands.RestartNetworkCmd;
import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientAddressCapacityException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.ResourceAllocationException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.network.Network.Capability;
import com.cloud.network.Network.Provider;
import com.cloud.network.Network.Service;
import com.cloud.network.Networks.TrafficType;
import com.cloud.user.Account;
import com.cloud.user.User;
import com.cloud.utils.Pair;
import com.cloud.vm.VirtualMachine;

public interface NetworkService {

    List<? extends Network> getIsolatedNetworksOwnedByAccountInZone(long zoneId, Account owner);

    IpAddress allocateIP(long networkId, Account ipOwner) throws ResourceAllocationException, InsufficientAddressCapacityException, ConcurrentOperationException;
    IpAddress allocateIP(long networkId, Account ipOwner, boolean isSystem) throws ResourceAllocationException, 
    InsufficientAddressCapacityException, ConcurrentOperationException;

    /**
     * Associates a public IP address for a router.
     * 
     * @param ipId
     *            - the command specifying ipAddress
     * @return ip address object
     * @throws ResourceAllocationException
     *             , InsufficientCapacityException
     */
    IpAddress associateIP(long ipId) throws ResourceAllocationException, InsufficientAddressCapacityException, 
    ConcurrentOperationException, ResourceUnavailableException;

    boolean disassociateIpAddress(long ipAddressId) throws InsufficientAddressCapacityException;

    Network createGuestNetwork(CreateNetworkCmd cmd) throws InsufficientCapacityException, ConcurrentOperationException,
    ResourceAllocationException;

    List<? extends Network> searchForNetworks(ListNetworksCmd cmd);

    boolean deleteNetwork(long networkId);

    boolean restartNetwork(RestartNetworkCmd cmd, boolean cleanup) throws ConcurrentOperationException,
    ResourceUnavailableException, InsufficientCapacityException;

    int getActiveNicsInNetwork(long networkId);

    Network getNetwork(long networkId);

    IpAddress getIp(long id);

    NetworkProfile convertNetworkToNetworkProfile(long networkId);

    Map<Service, Map<Capability, String>> getNetworkCapabilities(long networkId);

    boolean isNetworkAvailableInDomain(long networkId, long domainId);

    Long getDedicatedNetworkDomain(long networkId);

    Network updateGuestNetwork(long networkId, String name, String displayText, Account callerAccount, User callerUser,
            String domainSuffix, Long networkOfferingId, Boolean changeCidr);

    Integer getNetworkRate(long networkId, Long vmId);

    Network getSystemNetworkByZoneAndTrafficType(long zoneId, TrafficType trafficType);

    Map<Service, Set<Provider>> getNetworkOfferingServiceProvidersMap(long networkOfferingId);

    PhysicalNetwork createPhysicalNetwork(Long zoneId, String vnetRange, String networkSpeed, 
            List<String> isolationMethods, String broadcastDomainRange, Long domainId, List<String> tags, String name);

    List<? extends PhysicalNetwork> searchPhysicalNetworks(Long id, Long zoneId, String keyword, 
            Long startIndex, Long pageSize, String name);

    PhysicalNetwork updatePhysicalNetwork(Long id, String networkSpeed, List<String> tags, 
            String newVnetRangeString, String state);

    boolean deletePhysicalNetwork(Long id);

    List<? extends Service> listNetworkServices(String providerName);

    List<? extends Provider> listSupportedNetworkServiceProviders(String serviceName);

    PhysicalNetworkServiceProvider addProviderToPhysicalNetwork(Long physicalNetworkId, String providerName,
            Long destinationPhysicalNetworkId, List<String> enabledServices);

    List<? extends PhysicalNetworkServiceProvider> listNetworkServiceProviders(Long physicalNetworkId, String name,
            String state, Long startIndex, Long pageSize);

    PhysicalNetworkServiceProvider updateNetworkServiceProvider(Long id, String state, List<String> enabledServices);

    boolean deleteNetworkServiceProvider(Long id) throws ConcurrentOperationException, ResourceUnavailableException;

    PhysicalNetwork getPhysicalNetwork(Long physicalNetworkId);

    PhysicalNetwork getCreatedPhysicalNetwork(Long physicalNetworkId);

    PhysicalNetworkServiceProvider getPhysicalNetworkServiceProvider(Long providerId);

    PhysicalNetworkServiceProvider getCreatedPhysicalNetworkServiceProvider(Long providerId);

    long findPhysicalNetworkId(long zoneId, String tag, TrafficType trafficType);

    PhysicalNetworkTrafficType addTrafficTypeToPhysicalNetwork(Long physicalNetworkId, String trafficType, 
            String xenLabel, String kvmLabel, String vmwareLabel, String simulatorLabel, String vlan);

    PhysicalNetworkTrafficType getPhysicalNetworkTrafficType(Long id);

    PhysicalNetworkTrafficType updatePhysicalNetworkTrafficType(Long id, String xenLabel, String kvmLabel, String vmwareLabel);

    boolean deletePhysicalNetworkTrafficType(Long id);

    List<? extends PhysicalNetworkTrafficType> listTrafficTypes(Long physicalNetworkId);

    PhysicalNetwork getDefaultPhysicalNetworkByZoneAndTrafficType(long zoneId, TrafficType trafficType);

    Network getExclusiveGuestNetwork(long zoneId);

    List<Pair<TrafficType, String>> listTrafficTypeImplementor(ListTrafficTypeImplementorsCmd cmd);

    List<? extends Network> getIsolatedNetworksWithSourceNATOwnedByAccountInZone(long zoneId, Account owner);
    
    List<? extends Network> listNetworksByVpc(long vpcId);
    
    boolean addVmToNetwork(VirtualMachine vm, Network network);
    
    boolean removeVmFromNetwork(VirtualMachine vm, Network network);
    
    boolean isVmPartOfNetwork(long vmId, long ntwkId);

}
